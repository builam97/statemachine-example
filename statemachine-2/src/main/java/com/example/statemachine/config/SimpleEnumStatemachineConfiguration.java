package com.example.statemachine.config;

import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import com.example.statemachine.LoggingUtil.LoggingUtils;
import com.example.statemachine.enums.OrderEvents;
import com.example.statemachine.enums.OrderStates;

@Configuration
@EnableStateMachineFactory(name ="simple")
public class SimpleEnumStatemachineConfiguration extends StateMachineConfigurerAdapter<OrderStates, OrderEvents> {
	private static Logger logger = LoggingUtils.LOGGER;
	@Override
	public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
		transitions
				.withExternal().source(OrderStates.SUBMITTED).target(OrderStates.PAID).event(OrderEvents.PAY)
				.and()
				.withExternal().source(OrderStates.PAID).target(OrderStates.FULFILLED).event(OrderEvents.FULFILL)
				.and()
				.withExternal().source(OrderStates.SUBMITTED).target(OrderStates.CANCELLED).event(OrderEvents.CANCEL)
				.and()
				.withExternal().source(OrderStates.PAID).target(OrderStates.CANCELLED).event(OrderEvents.CANCEL);
	}

	@Override
	public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
		states
				.withStates()
				.initial(OrderStates.SUBMITTED)
				/*.stateEntry(OrderStates.SUBMITTED, context -> {
					Long orderId = Long.class.cast(context.getExtendedState().getVariables().getOrDefault("orderId", -1L));
					log.info("orderId is " + orderId + ".");
					log.info("entering submitted state!");
				})*/
				.state(OrderStates.PAID)
				.end(OrderStates.FULFILLED)
				.end(OrderStates.CANCELLED);
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {

		StateMachineListenerAdapter<OrderStates, OrderEvents> adapter = new StateMachineListenerAdapter<OrderStates, OrderEvents>() {
			@Override
			public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
				logger.info(String.format("stateChanged(from: %s, to: %s)", from + "", to + ""));
			}
		};
		config.withConfiguration()
				.autoStartup(false)
				.listener(adapter);
	}
}
