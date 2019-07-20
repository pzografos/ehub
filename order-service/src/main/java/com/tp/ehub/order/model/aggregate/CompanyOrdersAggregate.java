package com.tp.ehub.order.model.aggregate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tp.ehub.common.infra.model.AbstractAggregate;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.CompanyOrders;

public class CompanyOrdersAggregate extends AbstractAggregate<OrderEvent, CompanyOrders, UUID> {
	
	private Map<UUID, Long> stock = new HashMap<UUID, Long>();

	public CompanyOrdersAggregate(CompanyOrders rootEntity) {
		super(rootEntity);
	}

	@Override
	protected CompanyOrders mutate(CompanyOrders entity, OrderEvent event) {
		return new CompanyOrderReducer().apply(entity, event);
	}

	public Map<UUID, Long> getStock() {
		return stock;
	}

	public void updateStock(UUID productId, Long quantity) {
		stock.put(productId, quantity);
	}
}
