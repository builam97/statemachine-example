package com.example.statemachine.response;

import java.util.List;

public class OrderStateResponse {
	private String title;
	private String id;
	private List<TaskResponse> tasks;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<TaskResponse> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskResponse> tasks) {
		this.tasks = tasks;
	}
	public OrderStateResponse(String title, String id, List<TaskResponse> tasks) {
		super();
		this.title = title;
		this.id = id;
		this.tasks = tasks;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
