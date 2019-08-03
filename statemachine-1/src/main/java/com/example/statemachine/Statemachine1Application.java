package com.example.statemachine;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;

import com.example.statemachine.enums.BookEvents;
import com.example.statemachine.enums.BookStates;
import com.example.statemachine.utils.LoggingUtils;

@SpringBootApplication
@EnableStateMachine
public class Statemachine1Application implements CommandLineRunner {
	private static Logger logger = LoggingUtils.LOGGER;
	private StateMachine<BookStates, BookEvents> stateMachine;

	@Autowired
	public Statemachine1Application(StateMachine<BookStates, BookEvents> stateMachine) {
		this.stateMachine = stateMachine;
	}

	public static void main(String[] args) {
		SpringApplication.run(Statemachine1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		boolean returnAccepted = stateMachine.sendEvent(BookEvents.RETURN);
        logger.info("return accepted: " + returnAccepted);
        boolean borrowAccepted = stateMachine.sendEvent(BookEvents.BORROW);
        logger.info("borrow accepted: " + borrowAccepted);
        returnAccepted = stateMachine.sendEvent(BookEvents.RETURN);
        logger.info("return accepted: " + returnAccepted);
        boolean start = stateMachine.sendEvent(BookEvents.START_REPAIR);
        logger.info("start accepted: " + start);
        boolean end = stateMachine.sendEvent(BookEvents.END_REPAIR);
        logger.info("end accepted: " + end);
	}

}
