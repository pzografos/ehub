package com.tp.ehub.product.repository;

import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.aggregate.ProductCatalogueAggregate;
import com.tp.ehub.product.model.event.ProductEvent;
import com.tp.ehub.repository.aggregate.AbstractAggregateRepository;

@Dependent
public class ProductCatalogueRepository extends AbstractAggregateRepository<ProductCatalogueAggregate, ProductEvent, ProductCatalogue, UUID> {

	public ProductCatalogueRepository() {
		super();
	}

	@Override
	protected ProductCatalogueAggregate create(ProductCatalogue root) {
		return new ProductCatalogueAggregate(root);
	}

	@Override
	protected ProductCatalogue create(UUID id) {
		return new ProductCatalogue(id);
	}

}