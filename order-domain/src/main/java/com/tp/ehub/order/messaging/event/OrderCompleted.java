package com.tp.ehub.order.messaging.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Order.Completed")
public class OrderCompleted extends OrderEvent {

	public static final String NAME = "ORDER_COMPLETED";

	public OrderCompleted(UUID orderId) {
		super(orderId);
	}

	@Override
	public String getEventName() {
		return NAME;
	}

}
