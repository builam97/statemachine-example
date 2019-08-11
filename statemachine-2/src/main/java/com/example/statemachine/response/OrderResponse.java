package com.example.statemachine.response;

import java.util.Date;

import com.example.statemachine.entities.Order;

public class OrderResponse {
	private Long id;
	
	private Date datetime;

	private String state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public OrderResponse(Order order) {
		this.id = order.getId();
		this.datetime = order.getDatetime();
		this.state = order.getOrderState().toString();
	}
}
