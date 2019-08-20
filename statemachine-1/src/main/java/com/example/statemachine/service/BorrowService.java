package com.example.statemachine.service;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import com.example.statemachine.entities.Borrow;
import com.example.statemachine.enums.BookEvents;
import com.example.statemachine.enums.BookStates;
import com.example.statemachine.repository.BorrowRepository;

@Service
public class BorrowService {
	private final StateMachineFactory<BookStates, BookEvents> factory;
	
	private BorrowRepository borrowRepository;
	
	private static final String BORROW_ID_HEADER = "borrowId";

	public BorrowService(StateMachineFactory<BookStates, BookEvents> factory, BorrowRepository borrowRepository) {
		this.factory = factory;
		this.borrowRepository = borrowRepository;
	}
	
	Optional<Borrow> findById(Long id) {
		return borrowRepository.findById(id);
	}
	
	Borrow save(Borrow borrow) {
		return borrowRepository.save(borrow);	}
	
	StateMachine<BookStates, BookEvents> pay(Long orderId, String paymentConfirmationNumber) {
		StateMachine<BookStates, BookEvents> sm = this.build(orderId);

		Message<BookEvents> paymentMessage = MessageBuilder.withPayload(BookEvents.BORROW)
				.setHeader(BORROW_ID_HEADER, orderId)
				.setHeader("paymentConfirmationNumber", paymentConfirmationNumber)
				.build();

		sm.sendEvent(paymentMessage);
		// todo
		return sm;
	}
	
	StateMachine<BookStates, BookEvents> fulfill(Long orderId) {
		StateMachine<BookStates, BookEvents> sm = this.build(orderId);
		Message<BookEvents> fulfillmentMessage = MessageBuilder.withPayload(BookEvents.START_REPAIR)
				.setHeader(BORROW_ID_HEADER, orderId)
				.build();
		sm.sendEvent(fulfillmentMessage);
		return sm;
	}
	private StateMachine<BookStates, BookEvents> build(Long orderId) {
		Optional<Borrow> borrow = this.borrowRepository.findById(orderId);
		String borrowIdKey = Long.toString(borrow.get().getId());

		StateMachine<BookStates, BookEvents> sm = this.factory.getStateMachine(borrowIdKey);
		sm.stop();
		sm.getStateMachineAccessor()
				.doWithAllRegions(sma -> {

					sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<BookStates, BookEvents>() {

						@Override
						public void preStateChange(State<BookStates, BookEvents> state, Message<BookEvents> message, Transition<BookStates, BookEvents> transition, StateMachine<BookStates, BookEvents> stateMachine) {

							Optional.ofNullable(message).ifPresent(msg -> {

								Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(BORROW_ID_HEADER, -1L)))
										.ifPresent(orderId1 -> {
											Optional<Borrow> borrow = borrowRepository.findById(orderId1);
											borrow.get().setState(state.getId());
											borrowRepository.save(borrow.get());
										});
							});

						}
					});
					sma.resetStateMachine(new DefaultStateMachineContext<>(borrow.get().getState(), null, null, null));
				});
		sm.start();
		return sm;
	}
}
