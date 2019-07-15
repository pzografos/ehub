package com.tp.ehub.order.repository;

import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.common.infra.messaging.kafka.AbstractMessageStore;
import com.tp.ehub.product.messaging.event.ProductEvent;

public class OrderEventsStore extends AbstractMessageStore<UUID, ProductEvent> {

	@Inject
	public OrderEventsStore() {
		super(ProductEvent.class);
	}

}
