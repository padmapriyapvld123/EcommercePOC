package com.nineleaps.order.entity;

import java.io.Serializable;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;



@PrimaryKeyClass
public class OrderTablePrimaryKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@Column("id")
	private String id;

	@PrimaryKeyColumn(name = "customer_email", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	@Column("customer_email")
	private String customerEmail;
	
	

	public OrderTablePrimaryKey() {
		
	}

	public OrderTablePrimaryKey(String id, String customerEmail) {
		super();
		this.id = id;
		this.customerEmail = customerEmail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
}