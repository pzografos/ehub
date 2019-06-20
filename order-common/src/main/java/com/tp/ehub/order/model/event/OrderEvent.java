package com.tp.ehub.order.model.event;

import java.util.UUID;

import com.tp.ehub.model.event.AbstractEvent;

public abstract class OrderEvent extends AbstractEvent {

	protected UUID orderId;

	protected OrderEvent(UUID orderId) {
		this.orderId = orderId;
	}

	public UUID getOrderId() {
		return orderId;
	}
}
