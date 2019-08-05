package com.example.statemachine.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import com.example.statemachine.entities.Order;
import com.example.statemachine.enums.OrderEvents;
import com.example.statemachine.enums.OrderStates;
import com.example.statemachine.repository.OrderRepository;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	@Autowired
	@Qualifier("simple")
	private StateMachineFactory<OrderStates, OrderEvents> factory;

	private static final String ORDER_ID_HEADER = "orderId";

	OrderService(OrderRepository orderRepository, StateMachineFactory<OrderStates, OrderEvents> factory) {
		this.orderRepository = orderRepository;
		this.factory = factory;
	}

	public Optional<Order> byId(Long id) {
		return this.orderRepository.findById(id);
	}

	public Order create(Date when) {
		return this.orderRepository.save(new Order(when, OrderStates.SUBMITTED));
	}

	public StateMachine<OrderStates, OrderEvents> pay(Long orderId, String paymentConfirmationNumber) {
		StateMachine<OrderStates, OrderEvents> sm = this.build(orderId);

		Message<OrderEvents> paymentMessage = MessageBuilder.withPayload(OrderEvents.PAY)
				.setHeader(ORDER_ID_HEADER, orderId)
				.setHeader("paymentConfirmationNumber", paymentConfirmationNumber)
				.build();

		sm.sendEvent(paymentMessage);
		// todo
		return sm;
	}

	public StateMachine<OrderStates, OrderEvents> fulfill(Long orderId) {
		StateMachine<OrderStates, OrderEvents> sm = this.build(orderId);
		Message<OrderEvents> fulfillmentMessage = MessageBuilder.withPayload(OrderEvents.FULFILL)
				.setHeader(ORDER_ID_HEADER, orderId)
				.build();
		sm.sendEvent(fulfillmentMessage);
		return sm;
	}

	private StateMachine<OrderStates, OrderEvents> build(Long orderId) {
		Optional<Order> order = this.orderRepository.findById(orderId);
		String orderIdKey = Long.toString(order.get().getId());

		StateMachine<OrderStates, OrderEvents> sm = this.factory.getStateMachine(orderIdKey);
		sm.stop();
		sm.getStateMachineAccessor()
				.doWithAllRegions(sma -> {

					sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<OrderStates, OrderEvents>() {

						@Override
						public void preStateChange(State<OrderStates, OrderEvents> state, Message<OrderEvents> message, Transition<OrderStates, OrderEvents> transition, StateMachine<OrderStates, OrderEvents> stateMachine) {

							Optional.ofNullable(message).ifPresent(msg -> {

								Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(ORDER_ID_HEADER, -1L)))
										.ifPresent(orderId1 -> {
											Optional<Order> order1 = orderRepository.findById(orderId1);
											Order order2 = order1.get();
											order2.setOrderState(state.getId());
											orderRepository.save(order2);
										});
							});

						}
					});
					sma.resetStateMachine(new DefaultStateMachineContext<>(order.get().getOrderState(), null, null, null));
				});
		sm.start();
		return sm;
	}
}
