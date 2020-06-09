package com.nineleaps.order.model;

import java.io.Serializable;



public class Item implements Serializable{
	private static final long serialVersionUID = 1L;
	private String productId;
	private int quantity;
	private double price;
	
	public Item() {
		
	}

	public Item(String productId, int quantity, double price) {
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
