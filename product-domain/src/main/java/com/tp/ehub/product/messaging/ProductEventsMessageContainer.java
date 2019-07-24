package com.tp.ehub.product.messaging;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.tp.ehub.common.domain.messaging.container.AbstractMessageContainer;
import com.tp.ehub.product.messaging.event.ProductEvent;

@ApplicationScoped
@Named("product-events")
public class ProductEventsMessageContainer extends AbstractMessageContainer<UUID, ProductEvent> {

	@Inject
	public ProductEventsMessageContainer() {
		super("product-events", UUID.class, ProductEvent.class);
	}

}