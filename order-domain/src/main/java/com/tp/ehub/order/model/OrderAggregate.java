package com.tp.ehub.order.model;

import static com.tp.ehub.order.model.OrderStatus.CANCELLED;
import static com.tp.ehub.order.model.OrderStatus.COMPLETED;
import static com.tp.ehub.order.model.OrderStatus.CREATED;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tp.ehub.common.domain.model.AbstractAggregate;
import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCompleted;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;

public class OrderAggregate extends AbstractAggregate<OrderEvent, Order, UUID> {
	
	private Map<UUID, Long> stock = new HashMap<UUID, Long>();
	
	private Long version;

	public OrderAggregate(Order rootEntity) {
		super(rootEntity);
	}

	public Map<UUID, Long> getStock() {
		return stock;
	}

	public void updateStock(UUID productId, Long quantity) {
		stock.put(productId, quantity);
	}

	@Override
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	@Override
	public void apply(OrderEvent event) {
		switch(event.getEventName()) {
		case OrderCreated.NAME:
			applyOrderCreated((OrderCreated) event);
			break;
		case OrderCancelled.NAME:
			applyOrderCancelled((OrderCancelled) event);
			break;
		case OrderCompleted.NAME:
			applyOrderCompleted((OrderCompleted) event);
			break;
		default:
			throw new IllegalArgumentException(String.format("Unknown event %s", event.getEventName()));
		}
		newEvents.add(event);
	}
	
	private void applyOrderCreated(OrderCreated event) {
		rootEntity.setCompanyId(event.getCompanyId());
		rootEntity.setId(event.getOrderId());
		rootEntity.setStatus(CREATED);
		rootEntity.setBasket(event.getBasket());
	}
	
	private void applyOrderCancelled(OrderCancelled event) {
		rootEntity.setStatus(CANCELLED);
	}
	
	private void applyOrderCompleted(OrderCompleted event) {
		rootEntity.setStatus(COMPLETED);
	}
}
