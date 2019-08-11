package com.example.statemachine.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.statemachine.LoggingUtil.LoggingUtils;
import com.example.statemachine.entities.Order;
import com.example.statemachine.entities.Product;
import com.example.statemachine.enums.OrderEvents;
import com.example.statemachine.enums.OrderStates;
import com.example.statemachine.request.UpdateOrderRequest;
import com.example.statemachine.response.ListOrderResponse;
import com.example.statemachine.response.OrderResponse;
import com.example.statemachine.response.OrderStateResponse;
import com.example.statemachine.response.TaskResponse;
import com.example.statemachine.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	private static Logger logger = LoggingUtils.LOGGER;
	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> create(@RequestBody UpdateOrderRequest orderRequest) {
		Order order = new Order(orderRequest);
		order = this.orderService.create(order);
		OrderResponse orderResponse = new OrderResponse(order);
		return ResponseEntity.ok(orderResponse);
	}

	@PostMapping("/payment")
	public ResponseEntity<String> payment(@RequestParam(name = "orderId") Long orderId) {
		StateMachine<OrderStates, OrderEvents> paymentStateMachine = this.orderService.pay(orderId,
				UUID.randomUUID().toString());
		logger.info("after calling pay(): " + paymentStateMachine.getState().getId().name());
		logger.info("order: " + orderService.byId(orderId));
		return ResponseEntity.ok("Success");
	}

	@GetMapping("/change-state")
	public ResponseEntity<String> changeState(@RequestParam(name = "orderId") Long orderId,
			@RequestParam(name = "event") String event) {
		StateMachine<OrderStates, OrderEvents> stateMachine = null;
		if (OrderEvents.PAY.equals(OrderEvents.valueOf(event))) {
			stateMachine = this.orderService.pay(orderId, UUID.randomUUID().toString());
			logger.info("after calling pay(): " + stateMachine.getState().getId().name());
			logger.info("order: " + orderService.byId(orderId));
		} else if (OrderEvents.FULFILL.equals(OrderEvents.valueOf(event))) {
			stateMachine = orderService.fulfill(orderId);
			logger.info("after calling fulfill(): " + stateMachine.getState().getId().name());
			logger.info("order: " + orderService.byId(orderId));
		} else if (OrderEvents.CANCEL.equals(OrderEvents.valueOf(event))) {
			stateMachine = orderService.cancel(orderId);
		}
		return ResponseEntity.ok(stateMachine.getState().getId().name());
	}

	@GetMapping("/all")
	public ResponseEntity<ListOrderResponse> getAll() {
		List<TaskResponse> taskSubmit = this.orderService.findByState(OrderStates.SUBMITTED.toString()).stream().map(order -> {
			List<String> strName = order.getOrderProduct().stream().map(orderproduct -> orderproduct.getProduct().getName()).collect(Collectors.toList());
			return new TaskResponse(String.join(",", strName), order.getId());
			}).collect(Collectors.toList());
		OrderStateResponse orderStateSubmit = new OrderStateResponse(OrderStates.SUBMITTED.toString(), "Submit", taskSubmit);
		List<TaskResponse> taskPaid = this.orderService.findByState(OrderStates.PAID.toString()).stream().map(order -> {
			List<String> strName = order.getOrderProduct().stream().map(orderproduct -> orderproduct.getProduct().getName()).collect(Collectors.toList());
			return new TaskResponse(String.join(",", strName), order.getId());
			}).collect(Collectors.toList());
		OrderStateResponse orderStatePaid = new OrderStateResponse(OrderStates.PAID.toString(), OrderEvents.PAY.toString(), taskPaid);
		List<TaskResponse> taskFullfilled = this.orderService.findByState(OrderStates.FULFILLED.toString()).stream().map(order -> {
			List<String> strName = order.getOrderProduct().stream().map(orderproduct -> orderproduct.getProduct().getName()).collect(Collectors.toList());
			return new TaskResponse(String.join(",", strName), order.getId());
			}).collect(Collectors.toList());
		OrderStateResponse orderStateFullfilled = new OrderStateResponse(OrderStates.FULFILLED.toString(), OrderEvents.FULFILL.toString(), taskFullfilled);
		List<TaskResponse> taskCanceled = this.orderService.findByState(OrderStates.CANCELLED.toString()).stream().map(order -> {
			List<String> strName = order.getOrderProduct().stream().map(orderproduct -> orderproduct.getProduct().getName()).collect(Collectors.toList());
			return new TaskResponse(String.join(",", strName), order.getId());
			}).collect(Collectors.toList());
		OrderStateResponse orderStateCanceled = new OrderStateResponse(OrderStates.CANCELLED.toString(), OrderEvents.CANCEL.toString(), taskCanceled);
		ListOrderResponse listOrderResponse = new ListOrderResponse(orderStateSubmit, orderStatePaid, orderStateFullfilled, orderStateCanceled);
		return ResponseEntity.ok(listOrderResponse);
	}

}
