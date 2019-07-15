package com.tp.ehub.order.messaging;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.common.infra.messaging.kafka.container.AbstractTopic;
import com.tp.ehub.order.messaging.event.OrderEvent;

@ApplicationScoped
@Named("order-events")
public class OrderEventsTopic extends AbstractTopic<UUID, OrderEvent> {

	@Inject
	public OrderEventsTopic(@Named("objectMapper") ObjectMapper objectMapper) {
		super("order-events", objectMapper, UUID.class, OrderEvent.class);
	}

}
