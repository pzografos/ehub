package com.tp.ehub.product.model;

import java.util.UUID;

import com.tp.ehub.common.domain.model.AbstractAggregate;
import com.tp.ehub.product.messaging.event.ProductEvent;

public class ProductCatalogueAggregate extends AbstractAggregate<UUID, ProductEvent, ProductCatalogue> {

	private Long version;

	public ProductCatalogueAggregate(UUID key, ProductCatalogue rootEntity) {
		super(key, rootEntity);
	}
	
	@Override
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
