package com.example.statemachine.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.statemachine.enums.OrderStates;
import com.example.statemachine.request.UpdateOrderRequest;

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
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	private List<OrderProduct> orderProduct;

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

	public List<OrderProduct> getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(List<OrderProduct> orderProduct) {
		this.orderProduct = orderProduct;
	}

	public Order(UpdateOrderRequest orderRequest) {
		this.datetime = new Date();
		this.state = OrderStates.SUBMITTED.toString();
		String[] productIds = orderRequest.getProductIds().split(",");
		List<Long> listProductId = Arrays.asList(productIds).stream().map(product -> Long.parseLong(product))
				.collect(Collectors.toList());
		List<Product> listProduct = listProductId.stream().map(productId -> new Product(productId))
				.collect(Collectors.toList());
		this.orderProduct = listProduct.stream().map(product -> {
			OrderProduct orderproduct = new OrderProduct();
			orderproduct.setProduct(product);
			return orderproduct;
		}).collect(Collectors.toList());
	}
}
