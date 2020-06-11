package com.nineleaps.order.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.nineleaps.order.entity.OrderEntity;
import com.nineleaps.order.entity.OrderTablePrimaryKey;
import com.nineleaps.order.exception.NoContentException;
import com.nineleaps.order.model.Order;
import com.nineleaps.order.repository.OrderRepository;
import com.nineleaps.order.service.OrderService;


@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private KafkaTemplate<String, Order> kafkaTemplate;

	private static final String TOPIC = "Kafka_Order_test3";


	@PostMapping("/save")
	public ResponseEntity<Order> saveIntoOrderItemTable(@RequestBody Order order) {
		
		Order orderData =orderService.saveIntoOrderItemTable(order);
		 ObjectMapper mapper = new ObjectMapper();
	      //Converting the Object to JSONString
	      String jsonString =null;
		try {
			mapper.registerModule(new Jdk8Module());
			jsonString = mapper.writeValueAsString(orderData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		
		kafkaTemplate.send(TOPIC,order);

		return new ResponseEntity<>(orderData, HttpStatus.OK);
	}

	@GetMapping(path = "{id}/{customerEmail}")
	public ResponseEntity<Order> fetchRecordFromOrderTable(@PathVariable("id") String id,
			@PathVariable("customerEmail") String customerEmail){
		Order orderData = null;
		try {
			orderData = orderService.fetchRecordFromOrderTable(id, customerEmail);
		} catch (NoContentException e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(orderData, HttpStatus.OK);
		
	}
	
	@PutMapping("/updateOrder/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable("id") String id, @RequestBody Order order) {
		OrderTablePrimaryKey orderTablePrimaryKey = new OrderTablePrimaryKey();
		orderTablePrimaryKey.setCustomerEmail(order.getCustomerEmail());
		orderTablePrimaryKey.setId(id);
	  Optional<OrderEntity> orderData = orderRepository.findById(orderTablePrimaryKey);

	  if (orderData.isPresent()) {
		
		return new ResponseEntity<>(orderService.updateRecordIntoOrderTable(order), HttpStatus.OK);
		
		 } else {
		    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		  }
	}
	@DeleteMapping("/deleteOrder/{id}/{customerEmail}")
	public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") String id,
			@PathVariable("customerEmail") String customerEmail) {
	  try {
		 OrderTablePrimaryKey orderTablePrimaryKey = new OrderTablePrimaryKey();
		 orderTablePrimaryKey.setCustomerEmail(customerEmail);
		 orderTablePrimaryKey.setId(id);
		 
	    orderRepository.deleteById(orderTablePrimaryKey);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  } catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	  }
	}
	
	@GetMapping("/getAllOrders")
	public ResponseEntity<List<OrderEntity>> getAllOrders() {
	  try {
	    List<OrderEntity> orders = new ArrayList<OrderEntity>();

	   
	      orderRepository.findAll().forEach(orders::add);
	    
	    if (orders.isEmpty()) {
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }

	    return new ResponseEntity<>(orders, HttpStatus.OK);
	  } catch (Exception e) {
	    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	}

}
