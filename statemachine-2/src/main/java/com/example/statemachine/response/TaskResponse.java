package com.example.statemachine.response;

public class TaskResponse {
	private String title;
	
	private Long id;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TaskResponse(String title, Long id) {
		super();
		this.title = title;
		this.id = id;
	}
}
