package com.tp.ehub.product.model.aggregate;

import java.util.UUID;

import com.tp.ehub.common.infra.model.AbstractAggregate;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogue;

public class ProductCatalogueAggregate extends AbstractAggregate<ProductEvent, ProductCatalogue, UUID> {

	public ProductCatalogueAggregate(ProductCatalogue rootEntity) {
		super(rootEntity);
	}

	@Override
	protected ProductCatalogue mutate(ProductCatalogue catalogue, ProductEvent event) {
		return new ProductCatalogueReducer().apply(catalogue, event);
	}

}
