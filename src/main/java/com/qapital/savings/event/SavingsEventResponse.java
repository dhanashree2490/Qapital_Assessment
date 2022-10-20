package com.qapital.savings.event;

import java.time.LocalDate;
import java.util.List;

public class SavingsEventResponse {
	
	protected Long id;
	protected Long userId;
	protected LocalDate date;
	protected String errorCode;
	protected String errorMessage;
	
	protected List<SavingsEvent> events;

	public List<SavingsEvent> getEvents() {
		return events;
	}

	public void setEvents(List<SavingsEvent> events) {
		this.events = events;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
