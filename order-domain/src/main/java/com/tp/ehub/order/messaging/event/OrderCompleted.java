package com.tp.ehub.order.messaging.event;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(OrderCompleted.NAME)
public class OrderCompleted extends OrderEvent {

	public static final String NAME = "Order.Completed";

	private UUID orderId;
	
	private Map<UUID, Long> basket;

	public OrderCompleted(UUID orderId) {
		super(orderId);
	}
	
	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public Map<UUID, Long> getBasket() {
		return basket;
	}

	public void setBasket(Map<UUID, Long> basket) {
		this.basket = basket;
	}

	@Override
	public <P, R> R map(P parameter, BiFunctionVisitor<P, R> visitor) {
		return visitor.visit(parameter, this);
	}
	
	@Override
	protected void consume(ConsumerVisitor mapper) {
		mapper.accept(this);
	}
}
