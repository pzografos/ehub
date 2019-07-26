package com.tp.ehub.order.messaging.command;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to complete an existing order
 *
 */
@JsonTypeName(CompleteOrderCommand.NAME)
public class CompleteOrderCommand implements OrderCommand {

	public static final String NAME = "Commands.CompleteOrder";

	private UUID companyId;
	
	private UUID orderId;

	private ZonedDateTime timestamp;

	public CompleteOrderCommand() {

	}

	@Override
	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
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
