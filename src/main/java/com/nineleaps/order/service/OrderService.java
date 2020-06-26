package com.nineleaps.order.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nineleaps.order.entity.OrderEntity;
import com.nineleaps.order.entity.OrderTablePrimaryKey;
import com.nineleaps.order.exception.NoContentException;
import com.nineleaps.order.entity.ItemEntity;
import com.nineleaps.order.model.Order;
import com.nineleaps.order.model.Product;
import com.nineleaps.order.proxy.ProductProxy;
import com.nineleaps.order.model.Item;
import com.nineleaps.order.repository.OrderRepository;


@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired(required = true)
	private ProductProxy proxy;


	public Order saveIntoOrderItemTable(Order order) {
		OrderEntity entity = orderRepository.save(mapObjectToEntity(order));
		return mapEntityToObject(entity);
	}

	public OrderEntity mapObjectToEntity(Order order) {

		OrderTablePrimaryKey primaryKey = new OrderTablePrimaryKey();
		primaryKey.setId(order.getId());
		primaryKey.setCustomerEmail(order.getCustomerEmail());
		List<ItemEntity> itemEntityList = addValues(order);
		OrderEntity entity = new OrderEntity();
		entity.setPrimaryKey(primaryKey);
		entity.setItemEntity(itemEntityList);
		entity.setCustomerEmail(order.getCustomerEmail());
		entity.setCustomerName(order.getCustomerName());
		entity.setCustomerAddress(order.getCustomerAddress());
		entity.setTotal(order.getTotal());
		entity.setDate(order.getDate());
		return entity;
	}

	private List<ItemEntity> addValues(Order order) {
		List<ItemEntity> itemEntityList = new ArrayList<>();

		// ItemEntity itemEntity = new ItemEntity();
		int n = order.getItem().size();
		String productId = null;
		double price = 0.0;
		int quantity = 0;

		for (int i = 0; i < n; i++) {
			productId = order.getItem().get(i).getProductId();
			price = order.getItem().get(i).getPrice();
			quantity = order.getItem().get(i).getQuantity();
			ItemEntity itemEntity = new ItemEntity(productId, quantity, price);
			itemEntityList.add(itemEntity);

		}

		return itemEntityList;

	}

	public Order mapEntityToObject(OrderEntity entity) {

		List<Item> itemsList = new ArrayList<>();
		int n = entity.getItemEntity().size();
		String productId = null;
		double price = 0.0;
		int quantity = 0;
		for (int i = 0; i < n; i++) {
			productId = entity.getItemEntity().get(i).getProductId();
			price = entity.getItemEntity().get(i).getPrice();
			quantity = entity.getItemEntity().get(i).getQuantity();
			Item item = new Item(productId, quantity, price);
			itemsList.add(item);
		}

		Order table = new Order();
		table.setDate(entity.getDate());
		table.setId(entity.getPrimaryKey().getId());
		table.setCustomerEmail(entity.getPrimaryKey().getCustomerEmail());
		table.setCustomerName(entity.getCustomerName());
		table.setCustomerAddress(entity.getCustomerAddress());
		table.setTotal(entity.getTotal());
		table.setItem(itemsList);
		return table;
	}

	public Order fetchRecordFromOrderTable(String id, String customerEmail) throws NoContentException {
		OrderTablePrimaryKey primaryKey = new OrderTablePrimaryKey();
		primaryKey.setId(id);
		primaryKey.setCustomerEmail(customerEmail);

		Optional<OrderEntity> entity = orderRepository.findById(primaryKey);
		if (!entity.isPresent()) {
			throw new NoContentException(HttpStatus.NO_CONTENT);
		}
		return mapEntityToObject(entity.get());

	}

	public Order updateRecordIntoOrderTable(Order order) {
		OrderEntity entity = orderRepository.save(mapObjectToEntity(order));
		return mapEntityToObject(entity);

	}

	public Product saveIfProductAvailable(String productId) {
		Product product;
		return proxy.checkProductAvailability(productId);

	}

	public ResponseEntity<?> getIfProductAvailable(Order orderData) {

		if (orderData != null) {
			Product product = proxy.checkProductAvailability(orderData.getItem().get(0).getProductId());
			if (product != null)
				return new ResponseEntity<>(orderData, HttpStatus.OK);
			else

				return new ResponseEntity<>("Supplier is not available.", HttpStatus.NO_CONTENT);

		} else
			return new ResponseEntity<>("Product not found by id.", HttpStatus.NO_CONTENT);

	}
}