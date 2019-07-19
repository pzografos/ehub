package com.tp.ehub.order.repository;

import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.common.infra.sender.factory.kafka.AbstractMessageStore;
import com.tp.ehub.order.messaging.event.OrderEvent;

public class OrderEventsStore extends AbstractMessageStore<UUID, OrderEvent> {

	@Inject
	public OrderEventsStore() {
		super(OrderEvent.class);
	}

}
