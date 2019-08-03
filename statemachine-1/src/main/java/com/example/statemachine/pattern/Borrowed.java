package com.example.statemachine.pattern;

public class Borrowed implements States{

	@Override
	public void on() {
		System.out.println("get from BORROWED to IN_REPAIR");
	}

	@Override
	public void off() {
		System.out.println("get from BORROWED to AVAILABLE");
	}

}
