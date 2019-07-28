package com.tp.ehub.product.repository;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.common.domain.repository.EntityCache;
import com.tp.ehub.product.model.ProductCatalogue;

/**
 * Temp implementation
 * <p>
 * TODO: replace by Redis implementation
 */
@Dependent
public class ProductCatalogueEntityCache implements EntityCache<UUID, ProductCatalogue> {

	@Override
	public Optional<ProductCatalogue> get(UUID key) {
		return Optional.empty();
	}

	@Override
	public void cache(UUID key, ProductCatalogue entity) {

	}

	@Override
	public void evict(UUID id) {

	}

}
