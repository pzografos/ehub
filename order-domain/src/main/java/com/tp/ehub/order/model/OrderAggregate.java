package com.tp.ehub.order.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tp.ehub.common.domain.model.AbstractAggregate;
import com.tp.ehub.order.messaging.event.OrderEvent;

public class OrderAggregate extends AbstractAggregate<UUID, OrderEvent, Order> {
	
	private Map<UUID, Long> stock = new HashMap<UUID, Long>();
	
	private Long version;

	public OrderAggregate(UUID key, Order rootEntity) {
		super(key, rootEntity);
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
	
}
