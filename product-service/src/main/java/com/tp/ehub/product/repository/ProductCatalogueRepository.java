package com.tp.ehub.product.repository;

import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.common.infra.repository.AbstractPartitionedAggregateRepository;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

@Dependent
public class ProductCatalogueRepository extends AbstractPartitionedAggregateRepository<UUID, ProductEvent, ProductCatalogue, ProductCatalogueAggregate> {

	public ProductCatalogueRepository() {
		super();
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