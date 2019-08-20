package com.example.statemachine.pattern;

public class StateMachineMain {
	public static void main(String[] args) {
	FiniteStateMachine fsm = new FiniteStateMachine();
    int[] msgs = {1, 1, 0, 0 , 1};
    for (int msg : msgs) {
        if (msg == 0) {
            fsm.on();
        } else if (msg == 1) {
            fsm.off();
        }
    }
	}
}
