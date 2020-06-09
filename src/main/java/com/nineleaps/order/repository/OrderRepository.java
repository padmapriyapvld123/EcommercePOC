package com.nineleaps.order.repository;

import java.util.Optional;


import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;


import com.nineleaps.order.entity.OrderEntity;
import com.nineleaps.order.entity.OrderTablePrimaryKey;
import com.nineleaps.order.model.Order;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, OrderTablePrimaryKey> {

	
}
