package com.tp.ehub.order.model;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.factory.ObjectMapperFactory;
import com.tp.ehub.messaging.kafka.AbstractTopic;
import com.tp.ehub.order.model.event.OrderEvent;

public class OrderEventsTopic extends AbstractTopic<UUID, OrderEvent> {

	public static OrderEventsTopic get() {
		ObjectMapper mapper = ObjectMapperFactory.newInstance();
		return new OrderEventsTopic(mapper);
	}

	private OrderEventsTopic(ObjectMapper mapper) {
		super("order-events", mapper, UUID.class, OrderEvent.class);
	}
}
