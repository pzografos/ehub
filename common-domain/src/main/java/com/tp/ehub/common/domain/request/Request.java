package com.tp.ehub.common.domain.request;

import java.util.UUID;

public class Request {
	
	public enum Status{
		PENDING,
		ACCEPTED,
		FAILED
	}

	private UUID id;
	
	private Status status;
	
	private String message;

	public Request() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
