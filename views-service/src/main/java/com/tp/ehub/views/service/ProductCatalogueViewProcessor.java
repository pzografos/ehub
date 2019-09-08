package com.tp.ehub.views.service;

import java.util.UUID;

import com.tp.ehub.common.infra.service.AbstractViewProcessor;
import com.tp.ehub.common.views.model.ProductCatalogueView;
import com.tp.ehub.product.messaging.event.ProductEvent;

public class ProductCatalogueViewProcessor extends AbstractViewProcessor<UUID, ProductEvent, UUID, ProductCatalogueView> {
	
	public ProductCatalogueViewProcessor() {
		super("product_catalogue_view_processor_v1.0", ProductEvent.class, ProductCatalogueView.class);
	}

	@Override
	protected UUID getViewKey(ProductEvent event) {
		return event.getCompanyId();
	}

	@Override
	protected ProductCatalogueView createView(UUID key) {
		return new ProductCatalogueView(key);
	}

}
