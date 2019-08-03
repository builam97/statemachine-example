package com.example.statemachine.pattern;

public class Available implements States {

	@Override
	public void on() {
		System.out.println("get from AVAILABLE to BORROWED");
	}

	@Override
	public void off() {
		System.out.println("get from AVAILABLE to EMPTY STATE");
	}

}
