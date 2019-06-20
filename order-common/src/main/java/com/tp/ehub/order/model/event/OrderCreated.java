package com.tp.ehub.order.model.event;

import java.util.UUID;

public class OrderCreated extends OrderEvent {

	public static final String NAME = "ORDER_CREATED";
	
	public OrderCreated(UUID orderId) {
		super(orderId);
	}

	@Override
	public String getEventName() {
		return NAME;
	}
	
}
