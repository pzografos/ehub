package com.tp.ehub.command;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to create a new order
 *
 */
@JsonTypeName(CreateOrderCommand.NAME)
public class CreateOrderCommand implements Command {

	public static final String NAME = "Commands.CreateOrder";

	private UUID companyId;

	private Map<UUID, Long> basket;

	private ZonedDateTime timestamp;

	public CreateOrderCommand() {

	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	public Map<UUID, Long> getBasket() {
		return basket;
	}

	public void setBasket(Map<UUID, Long> basket) {
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
	public String getKey() {
		return companyId.toString();
	}

	@Override
	public void consume(ConsumerVisitor mapper) {
		mapper.accept(this);
	}
}
