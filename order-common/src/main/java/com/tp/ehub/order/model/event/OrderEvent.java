package com.tp.ehub.order.model.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.model.event.AbstractEvent;
import com.tp.ehub.serialization.JsonMessage;

@JsonMessage
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
	    @Type(value = OrderCreated.class, name = "Order.Created")})
public abstract class OrderEvent extends AbstractEvent<UUID> {

	protected UUID orderId;

	protected OrderEvent(UUID orderId) {
		this.orderId = orderId;
	}

	public UUID getOrderId() {
		return orderId;
	}
}
