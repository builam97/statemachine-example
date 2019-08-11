package com.example.statemachine.response;

import java.util.ArrayList;
import java.util.List;

public class ListOrderResponse {
	private List<OrderStateResponse> orderState;
	
	public ListOrderResponse(OrderStateResponse submits, OrderStateResponse paid, OrderStateResponse fullfilled,
			OrderStateResponse canceled) {
		super();
		List<OrderStateResponse> orderstate = new ArrayList<>();
		orderstate.add(submits);
		orderstate.add(paid);
		orderstate.add(fullfilled);
		orderstate.add(canceled);
		this.orderState = orderstate;
	}

	public List<OrderStateResponse> getOrderState() {
		return orderState;
	}

	public void setOrderState(List<OrderStateResponse> orderState) {
		this.orderState = orderState;
	}
}
