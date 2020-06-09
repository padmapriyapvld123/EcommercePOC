package com.nineleaps.order.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("order_items")

public class OrderEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private OrderTablePrimaryKey primaryKey;
	@Column("date")
	private String date;
	@Column("customer_name")
	private String customerName;
	@Column("customer_email")
	private String customerEmail;
	@Column("customer_address")
	private String customerAddress;
	@Column("total")
	private double total;
	@CassandraType(type = com.datastax.driver.core.DataType.Name.UDT, userTypeName = "item_entity")
	@Column("item_entity")
	private List<ItemEntity> itemEntity;
	
	public OrderEntity() {
		
	}
	public OrderEntity(OrderTablePrimaryKey primaryKey, String date, String customerName, String customerEmail,
			String customerAddress, double total, List<ItemEntity> itemEntity) {
		super();
		this.primaryKey = primaryKey;
		this.date = date;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.customerAddress = customerAddress;
		this.total = total;
		this.itemEntity = itemEntity;
	}
	public OrderTablePrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(OrderTablePrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public List<ItemEntity> getItemEntity() {
		return itemEntity;
	}
	public void setItemEntity(List<ItemEntity> itemEntity) {
		this.itemEntity = itemEntity;
	}
	
	
		
}