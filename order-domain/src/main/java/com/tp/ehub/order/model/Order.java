package com.tp.ehub.order.model;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import com.tp.ehub.common.domain.model.Entity;

public class Order implements Entity<UUID> {

	private UUID id;
	
	private UUID companyId;

	private Map<String, Integer> basket;

	private ZonedDateTime timestamp;

	public Order() {

	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public UUID getId() {
		return id;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	public Map<String, Integer> getBasket() {
		return basket;
	}

	public void setBasket(Map<String, Integer> basket) {
		this.basket = basket;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
