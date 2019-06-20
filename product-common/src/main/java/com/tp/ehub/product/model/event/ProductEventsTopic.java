package com.tp.ehub.product.model.event;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.factory.ObjectMapperFactory;
import com.tp.ehub.messaging.kafka.AbstractTopic;

public class ProductEventsTopic extends AbstractTopic<UUID, ProductEvent>{

	public static ProductEventsTopic get() {
		ObjectMapper mapper = ObjectMapperFactory.newInstance();
		mapper.registerModule(new ProductEventsModule());
		return new ProductEventsTopic(mapper);
	}
	
	private ProductEventsTopic(ObjectMapper mapper) {
		super("product-events", mapper, UUID.class, ProductEvent.class);
	}
}