package com.tp.ehub.order.messaging.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Order.Cancelled")
public class OrderCancelled extends OrderEvent {

	public static final String NAME = "ORDER_CANCELLED";

	public OrderCancelled(UUID orderId) {
		super(orderId);
	}

	@Override
	public String getEventName() {
		return NAME;
	}

}
