package com.tp.ehub.command;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to cancel an existing order
 *
 */
@JsonTypeName("Commands.CancelOrder")
public class CancelOrderCommand implements Command {

	public static final String NAME = "CANCEL_ORDER";

	private UUID companyId;
	
	private UUID orderId;

	private ZonedDateTime timestamp;

	public CancelOrderCommand() {

	}

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

	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
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
