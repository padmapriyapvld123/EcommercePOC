package com.nineleaps.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class OrderRequest implements Serializable{
		private static final long serialVersionUID = 1L;
		private String id;
		private String date;
		private String customerName;
		private String customerEmail;
		private String customerAddress;
		private List<Item> item;
		private double total;
		
		public OrderRequest() {
			
		}
		
		public OrderRequest(String id, String date, String customerName, String customerEmail, String customerAddress,
				List<Item> item, double total) {
			super();
			this.id = id;
			this.date = date;
			this.customerName = customerName;
			this.customerEmail = customerEmail;
			this.customerAddress = customerAddress;
			this.item = item;
			this.total = total;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
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
		public List<Item> getItem() {
			return item;
		}
		public void setItem(List<Item> item) {
			this.item = item;
		}
		public double getTotal() {
			return total;
		}
		public void setTotal(double total) {
			this.total = total;
		}
		
		
					    
}
