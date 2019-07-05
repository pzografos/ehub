package com.tp.ehub.order.model;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.factory.ObjectMapperFactory;
import com.tp.ehub.messaging.kafka.AbstractTopic;
import com.tp.ehub.messaging.kafka.service.Topic;
import com.tp.ehub.order.model.event.OrderEvent;
import jdk.jfr.Name;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;

@ApplicationScoped
@Named("order-events")
public class OrderEventsTopic extends AbstractTopic<UUID, OrderEvent> {

	@Inject
	public OrderEventsTopic(@Named("objectMapper") ObjectMapper objectMapper) {
		super("order-events", objectMapper, UUID.class, OrderEvent.class);
	}

}
