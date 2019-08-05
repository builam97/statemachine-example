package com.example.statemachine.LoggingUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import com.example.statemachine.enums.OrderEvents;
import com.example.statemachine.enums.OrderStates;


public class LoggingUtils {
	public static final Logger LOGGER = LoggerFactory.getLogger("STATE MACHINE");

    public static String getStateInfo(State<OrderStates, OrderEvents> state) {
        return state != null ? state.getId().name() : "EMPTY STATE";
    }

    public static String getTransitionInfo(Transition<OrderStates, OrderEvents> t) {
        return String.format("[%s: %s]",
                t.getSource() != null ? t.getSource().getId() : "EMPTY",
                t.getTarget() != null ? t.getTarget().getId() : "EMPTY"
        );
    }
}
