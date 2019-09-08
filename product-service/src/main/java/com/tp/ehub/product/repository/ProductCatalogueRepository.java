package com.tp.ehub.product.repository;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import com.tp.ehub.common.infra.repository.AbstractPartitionedAggregateRepository;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

@ApplicationScoped
public class ProductCatalogueRepository extends AbstractPartitionedAggregateRepository<UUID, ProductEvent, ProductCatalogue, ProductCatalogueAggregate> {

	public ProductCatalogueRepository() {
		super(ProductEvent.class, ProductCatalogue.class);
	}

	@Override
	protected ProductCatalogueAggregate create(UUID key, ProductCatalogue root) {
		return new ProductCatalogueAggregate(key, root);
	}

	@Override
	protected ProductCatalogue create(UUID id) {
		return new ProductCatalogue(id);
	}

}