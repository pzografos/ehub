package com.tp.ehub.command;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Commands.CreateOrder")
public class CreateOrderCommand implements Command {

	public static final String NAME = "CREATE_ORDER";
	
	private UUID companyId;
	
	private Map<String, Integer> basket;
	
	private ZonedDateTime timestamp;

	public CreateOrderCommand() {

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

	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	@JsonIgnore
	public String getCommandName() {
		return NAME;
	}
	
	@Override
	@JsonIgnore
	public String getKey() {
		return companyId.toString();
	}

}
