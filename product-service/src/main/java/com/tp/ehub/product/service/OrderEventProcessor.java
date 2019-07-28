package com.tp.ehub.product.service;

import java.util.UUID;

import com.tp.ehub.common.infra.service.AbstractEventProcessor;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

public class OrderEventProcessor extends AbstractEventProcessor<UUID, OrderEvent, UUID, ProductEvent, ProductCatalogue, ProductCatalogueAggregate> {
	
	public OrderEventProcessor() {
		super("product_order_event_receiver_v1.0", OrderEvent.class);
	}

	@Override
	protected String getAggregatePartitionKey(OrderEvent event) {
		return event.getCompanyId().toString();
	}

	@Override
	protected UUID getAggregateKey(OrderEvent event) {
		return event.getCompanyId();
	}

}
