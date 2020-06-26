package com.nineleaps.order.controller;

import java.util.ArrayList
;


import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.RestController;
import com.nineleaps.order.entity.OrderEntity;
import com.nineleaps.order.entity.OrderTablePrimaryKey;
import com.nineleaps.order.exception.NoContentException;
import com.nineleaps.order.model.Item;
import com.nineleaps.order.model.Order;
import com.nineleaps.order.model.OrderResponse;
import com.nineleaps.order.model.Product;
import com.nineleaps.order.proxy.ProductProxy;
import com.nineleaps.order.repository.OrderRepository;
import com.nineleaps.order.service.OrderService;


@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired(required=true)
	private ProductProxy proxy;

	
	private OrderService orderService;
	
	@Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

	@Autowired
	private OrderRepository orderRepository;
	
	

	@Autowired
	private KafkaTemplate<String, Order> kafkaTemplate;

	
	
	private static final String TOPIC = "Kafka_Order_test3";

	final Logger logger = Logger.getLogger(OrderController.class);

	@PostMapping("/save")

	public ResponseEntity<?> saveIntoOrderItemTable(@RequestBody Order order) {

		int n = order.getItem().size();

		for (int i = 0; i < n; i++) {

		//	Product product = proxy.checkProductAvailability(order.getItem().get(i).getProductId());
			Product product =orderService.saveIfProductAvailable(order.getItem().get(i).getProductId());
			if (product!=null && product.getProductId() != null) {

				Order orderData = orderService.saveIntoOrderItemTable(order);
				kafkaTemplate.send(TOPIC, order);

				return new ResponseEntity<>(orderData, HttpStatus.OK);
			} else {
				OrderResponse orderResponse = new OrderResponse();
				orderResponse.setResponse("Product Not in Stock");

				return new ResponseEntity<>(orderResponse, HttpStatus.FORBIDDEN);

			}
		}
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setResponse("No Products Ordered");
		return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);

	}

	@GetMapping(path = "{id}/{customerEmail}")
	public ResponseEntity<?> fetchRecordFromOrderTable(@PathVariable("id") String id,
			@PathVariable("customerEmail") String customerEmail) {
		Order orderData = null;
		ResponseEntity<?> orderDataResult = null;
		try {
			
			orderData = orderService.fetchRecordFromOrderTable(id, customerEmail);
			orderDataResult = orderService.getIfProductAvailable(orderData);
			
			
		} catch (NoContentException e) {
			return new ResponseEntity<>("No Content", HttpStatus.NO_CONTENT);
		}

		return orderDataResult;

	}

	@PutMapping("/updateOrder/{id}")
	public ResponseEntity<?> updateOrder(@PathVariable("id") String id, @RequestBody Order order) {
		OrderTablePrimaryKey orderTablePrimaryKey = new OrderTablePrimaryKey();
		orderTablePrimaryKey.setCustomerEmail(order.getCustomerEmail());
		orderTablePrimaryKey.setId(id);
		Optional<OrderEntity> orderData = orderRepository.findById(orderTablePrimaryKey);

		if (orderData.isPresent()) {

			return new ResponseEntity<>(orderService.updateRecordIntoOrderTable(order), HttpStatus.OK);

		} else {
			return new ResponseEntity<>("Order Data not Present", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deleteOrder/{id}/{customerEmail}")
	public ResponseEntity<?> deleteOrder(@PathVariable("id") String id,
			@PathVariable("customerEmail") String customerEmail) {
		try {
			OrderTablePrimaryKey orderTablePrimaryKey = new OrderTablePrimaryKey();
			orderTablePrimaryKey.setCustomerEmail(customerEmail);
			orderTablePrimaryKey.setId(id);

			orderRepository.deleteById(orderTablePrimaryKey);
			return new ResponseEntity<>("No Content", HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/getAllOrders")
	public ResponseEntity<?> getAllOrders() {
		try {
			List<OrderEntity> orders = new ArrayList<OrderEntity>();

			orderRepository.findAll().forEach(orders::add);

			if (orders.isEmpty()) {
				return new ResponseEntity<>("No Content", HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(orders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
