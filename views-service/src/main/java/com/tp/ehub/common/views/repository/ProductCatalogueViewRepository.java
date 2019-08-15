package com.tp.ehub.common.views.repository;

import java.util.UUID;

import com.tp.ehub.common.infra.repository.redis.AbstractViewRepository;
import com.tp.ehub.common.views.model.ProductCatalogueView;

public class ProductCatalogueViewRepository extends AbstractViewRepository<UUID, ProductCatalogueView>{

	protected ProductCatalogueViewRepository() {
		super(ProductCatalogueView.class);
	}

	@Override
	protected ProductCatalogueView getNewView(UUID key) {
		return new ProductCatalogueView(key);
	}
}
