package com.example.statemachine.pattern;

//1. Create a "wrapper" class that models the state machine
public class FiniteStateMachine {
	 // 2. states
    private States[] states = {new Available(), new Borrowed(), new InRepair()};
    // 4. transitions
    private int[][] transition = {{1,0}, {2,1}, {1,2}};
    // 3. current
    private int current = 0;

    private void next(int msg) {
        current = transition[current][msg];
    }

    // 5. All client requests are simply delegated to the current state object
    public void on() {
        states[current].on();
        next(0);
    }

    public void off() {
        states[current].off();
        next(1);
    }
}
