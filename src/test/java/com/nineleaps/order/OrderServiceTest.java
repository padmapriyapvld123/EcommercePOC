package com.nineleaps.order;

import org.cassandraunit.spring.CassandraDataSet;


import org.cassandraunit.spring.CassandraUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nineleaps.order.constants.JsonConstants;
import com.nineleaps.order.controller.OrderController;
import com.nineleaps.order.entity.ItemEntity;
import com.nineleaps.order.entity.OrderEntity;
import com.nineleaps.order.entity.OrderTablePrimaryKey;
import com.nineleaps.order.model.Order;
import com.nineleaps.order.model.Product;
import com.nineleaps.order.repository.OrderRepository;
import com.nineleaps.order.service.OrderService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
@SpringBootTest({ "spring.data.cassandra.port=9042", "spring.data.cassandra.keyspace-name=cycling1" })
@EnableAutoConfiguration
@ComponentScan
@ContextConfiguration
@CassandraDataSet(value = { "cassandra-init.sh" }, keyspace = "cycling1")
@CassandraUnit
public class OrderServiceTest {

	private MockMvc mockMvc;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository OrderRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private OrderController OrderController;

	private OrderService mockOrderService = null;

	@BeforeEach
	public void init() {
		OrderController.setOrderService(orderService);

		this.mockMvc = MockMvcBuilders.standaloneSetup(OrderController).build();
		// mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

	}

	@Test
	@DisplayName("saveOrder")
	public void saveOrder() throws Exception {

		Order OrderResult = null;
		List<Product> productList = new ArrayList<>();
		String mockOrderJson = JsonConstants.mockOrderJson;
		Product product = new Product("P001", "Table", 100.0, "Black Table", "test");
		productList.add(product);
		
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(mockOrderJson, Order.class);

		mockOrderService = Mockito.mock(OrderService.class);

		when(mockOrderService.saveIfProductAvailable(order.getItem().get(0).getProductId())).thenReturn(product);

		OrderResult = orderService.saveIntoOrderItemTable(order);

		assertEquals(OrderResult.getItem().get(0).getProductId(), order.getItem().get(0).getProductId());

	}

	@Test
	@DisplayName("getOrderById")
	public void getOrderById() throws Exception {
		Order OrderResult = null;

		String mockOrderJson = JsonConstants.mockOrderJson;

		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(mockOrderJson, Order.class);

		mockOrderService = Mockito.mock(OrderService.class);

		when(mockOrderService.getIfProductAvailable(order)).thenReturn(new ResponseEntity(order, HttpStatus.OK));

		OrderResult = orderService.fetchRecordFromOrderTable(order.getId(),order.getCustomerEmail());

		assertEquals(OrderResult.getItem().get(0).getProductId(), order.getItem().get(0).getProductId());
	}

	@Test
	@DisplayName("getAllOrders")
	public void getAllOrders() throws Exception {

		String mockOrderJson = JsonConstants.mockOrderJson;

		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(mockOrderJson, Order.class);

		mockMvc.perform(get("/order/getAllOrders").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].customerEmail").value(order.getCustomerEmail())).andExpect(status().isOk());
				
	}

	@Test
	public void updateOrderAPI() throws Exception {

		Order orderResult = null;
		List<Product> productList = new ArrayList<>();
		String mockOrderJson = JsonConstants.mockOrderJson;

		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(mockOrderJson, Order.class);

		Product product = new Product("P001", "Table", 100.0, "Black Table", "test");
		productList.add(product);

		mockOrderService = Mockito.mock(OrderService.class);

		when(mockOrderService.saveIfProductAvailable(order.getItem().get(0).getProductId())).thenReturn(product);

		mockMvc.perform(MockMvcRequestBuilders.put("/order/updateOrder/{id}", "6").content(asJsonString(order))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
				
	}

	@Test
	public void deleteSupplierAPI() throws Exception {
		
		String mockOrderJson = JsonConstants.mockOrderJson;
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(mockOrderJson, Order.class);
		String orderId = order.getId();
		
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/order/deleteOrder/{id}/{customerEmail}",orderId,order.getCustomerEmail()))
		       .andExpect(status().isNoContent());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}