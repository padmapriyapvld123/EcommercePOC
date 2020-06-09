package com.nineleaps.order.entity;

import java.io.Serializable;


import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;


@org.springframework.data.cassandra.core.mapping.UserDefinedType("item_entity")

public class ItemEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column("product_id")
	private String productId;
	@Column("quantity")
	private int quantity;
	@Column("price")
	private double price;
	
	public ItemEntity() {
		
	}
	
	public ItemEntity(String productId, int quantity, double price) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	


}
