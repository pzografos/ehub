package com.tp.ehub.common.views.service;

import java.util.UUID;

import com.tp.ehub.common.infra.service.AbstractViewProcessor;
import com.tp.ehub.common.views.model.ProductCatalogueView;
import com.tp.ehub.product.messaging.event.ProductEvent;

public class ProductCatalogueViewProcessor extends AbstractViewProcessor<UUID, ProductEvent, UUID, ProductCatalogueView> {
	
	public ProductCatalogueViewProcessor(String consumerId, Class<ProductEvent> messageClass) {
		super("product_catalogue_view_event_receiver_v1.0", ProductEvent.class);
	}

	@Override
	protected UUID getViewKey(ProductEvent event) {
		return event.getCompanyId();
	}

}
