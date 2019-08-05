package com.example.statemachine.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.statemachine.enums.OrderStates;

@Entity
@Table(name ="orders")
public class Order {
	@Id
	@GeneratedValue
	@Column(name ="id")
	private Long id;
	
	@Column(name ="datetime")
	private Date datetime;

	@Column(name ="state")
	private String state;

	public Order(Date d, OrderStates os) {
		this.datetime = d;
		this.setOrderState(os);
	}

	public OrderStates getOrderState() {
		return OrderStates.valueOf(this.state);
	}

	public void setOrderState(OrderStates s) {
		this.state = s.name();
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order() {
		super();
	}
	
}
