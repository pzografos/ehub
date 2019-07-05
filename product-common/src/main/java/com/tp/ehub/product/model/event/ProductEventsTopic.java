package com.tp.ehub.product.model.event;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.factory.ObjectMapperFactory;
import com.tp.ehub.messaging.kafka.AbstractTopic;
import com.tp.ehub.messaging.kafka.service.Topic;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
@Named("product-events")
public class ProductEventsTopic extends AbstractTopic<UUID, ProductEvent> {

	@Inject
	public ProductEventsTopic(@Named("objectMapper") ObjectMapper objectMapper) {
		super("product-events", objectMapper, UUID.class, ProductEvent.class);
	}

}