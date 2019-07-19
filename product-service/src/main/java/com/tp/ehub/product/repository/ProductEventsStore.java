package com.tp.ehub.product.repository;

import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.common.infra.sender.factory.kafka.AbstractMessageStore;
import com.tp.ehub.product.messaging.event.ProductEvent;

public class ProductEventsStore extends AbstractMessageStore<UUID, ProductEvent> {

	@Inject
	public ProductEventsStore() {
		super(ProductEvent.class);
	}

}
