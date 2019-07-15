package com.tp.ehub.product.messaging;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.common.infra.messaging.kafka.container.AbstractTopic;
import com.tp.ehub.product.messaging.event.ProductEvent;

@ApplicationScoped
@Named("product-events")
public class ProductEventsTopic extends AbstractTopic<UUID, ProductEvent> {

	@Inject
	public ProductEventsTopic(@Named("objectMapper") ObjectMapper objectMapper) {
		super("product-events", objectMapper, UUID.class, ProductEvent.class);
	}

}