package com.example.statemachine.pattern;

public class InRepair implements States{

	@Override
	public void on() {
		// TODO Auto-generated method stub
		System.out.println("get from IN_REPAIR to EMPTY STATE");
	}

	@Override
	public void off() {
		// TODO Auto-generated method stub
		System.out.println("get from IN_REPAIR to BORROWED");
	}

}
