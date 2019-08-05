package com.example.statemachine.run;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.example.statemachine.LoggingUtil.LoggingUtils;
import com.example.statemachine.entities.Order;
import com.example.statemachine.enums.OrderEvents;
import com.example.statemachine.enums.OrderStates;
import com.example.statemachine.service.OrderService;


@Component
class Runner implements ApplicationRunner {
	private static Logger logger = LoggingUtils.LOGGER;
	@Autowired
	private OrderService orderService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		Order order = this.orderService.create(new Date());

		StateMachine<OrderStates, OrderEvents> paymentStateMachine = this.orderService.pay(order.getId(), UUID.randomUUID().toString());
		logger.info("after calling pay(): " + paymentStateMachine.getState().getId().name());
		logger.info("order: " + orderService.byId(order.getId()));

		StateMachine<OrderStates, OrderEvents> fulfilledStateMachine = orderService.fulfill(order.getId());
		logger.info("after calling fulfill(): " + fulfilledStateMachine.getState().getId().name());
		logger.info("order: " + orderService.byId(order.getId()));


	}

	Runner(OrderService orderService) {
		this.orderService = orderService;
	}
}
