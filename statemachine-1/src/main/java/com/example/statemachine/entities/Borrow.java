package com.example.statemachine.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.statemachine.enums.BookStates;

@Entity
@Table(name = "borrow")
public class Borrow {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	
	@Column(name = "datetime")
	private String dateTime;
	
	@Column(name = "state")
	private String state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public BookStates getState() {
		return BookStates.valueOf(this.state);
	}

	public void setState(BookStates state) {
		this.state = state.name();
	}
}
