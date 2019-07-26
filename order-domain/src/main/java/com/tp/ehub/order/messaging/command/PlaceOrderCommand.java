package com.tp.ehub.order.messaging.command;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to place a new order
 *
 */
@JsonTypeName(PlaceOrderCommand.NAME)
public class PlaceOrderCommand implements OrderCommand {

	public static final String NAME = "Commands.PlaceOrder";

	private UUID orderId;

	private UUID companyId;

	private Map<UUID, Long> basket;

	private ZonedDateTime timestamp;

	public PlaceOrderCommand() {

	}

	@Override
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
	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
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
	public UUID getKey() {
		return orderId;
	}

	@Override
	public <P, R> R map(P parameter, BiFunctionVisitor<P, R> visitor) {
		return visitor.visit(parameter, this);
	}
}
