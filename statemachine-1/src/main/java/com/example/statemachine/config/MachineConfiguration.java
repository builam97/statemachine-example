package com.example.statemachine.config;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.state.State;

import com.example.statemachine.enums.BookEvents;
import com.example.statemachine.enums.BookStates;
import com.example.statemachine.listener.LoggingMachineListener;
import com.example.statemachine.utils.LoggingUtils;

@Configuration
@EnableStateMachine
public class MachineConfiguration extends EnumStateMachineConfigurerAdapter<BookStates, BookEvents>{
	 private static final Logger LOGGER = LoggingUtils.LOGGER;
	 
	 @Override
	    public void configure(StateMachineStateConfigurer<BookStates, BookEvents> states) throws Exception {
	        states.withStates().initial(BookStates.AVAILABLE)
	                .state(BookStates.AVAILABLE, entryAction())
	                .state(BookStates.BORROWED, entryAction())
	                .state(BookStates.IN_REPAIR, entryAction());
//	        states.withStates()
//	                .initial(BookStates.IN_REPAIR)
//	                .states(EnumSet.allOf(BookStates.class));
	    }

	    @Override
	    public void configure(StateMachineTransitionConfigurer<BookStates, BookEvents> transitions) throws Exception {
	        transitions
	                .withExternal()
	                .source(BookStates.AVAILABLE)
	                .target(BookStates.BORROWED)
	                .event(BookEvents.BORROW)
	                .and()
	                .withExternal()
	                .source(BookStates.BORROWED)
	                .target(BookStates.AVAILABLE)
	                .event(BookEvents.RETURN)
	                .and()
	                .withExternal()
	                .source(BookStates.AVAILABLE)
	                .target(BookStates.IN_REPAIR)
	                .event(BookEvents.START_REPAIR)
	                .and()
	                .withExternal()
	                .source(BookStates.IN_REPAIR)
	                .target(BookStates.AVAILABLE)
	                .event(BookEvents.END_REPAIR);
	    }

	    @Override
	    public void configure(StateMachineConfigurationConfigurer<BookStates, BookEvents> config) throws Exception {
	        config.withConfiguration()
	                .autoStartup(true)
	                .listener(new LoggingMachineListener())
	        ;
	    }

	    @Bean
	    public Action<BookStates, BookEvents> entryAction() {
	        return ctx -> LOGGER.info("Entry action {} to get from {} to {}",
	                ctx.getEvent(),
	                LoggingUtils.getStateInfo(ctx.getSource()),
	                LoggingUtils.getStateInfo(ctx.getTarget()));
	    }
//	
//	    @Bean
//	    public Action<BookStates, BookEvents> exitAction() {
//	        return ctx -> LOGGER.info("Exit action {} to get from {} to {}",
//	                ctx.getEvent(), LoggingUtils.getStateInfo(ctx.getSource()), LoggingUtils.getStateInfo(ctx.getTarget()));
//	    }
}
