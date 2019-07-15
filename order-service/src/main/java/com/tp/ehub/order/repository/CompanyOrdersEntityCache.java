package com.tp.ehub.order.repository;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.common.domain.repository.RootEntityCache;
import com.tp.ehub.product.model.ProductCatalogue;

/**
 * Temp implementation
 * <p>
 * TODO: replace by Redis implementation
 */
@Dependent
public class CompanyOrdersEntityCache implements RootEntityCache<UUID, ProductCatalogue> {

	@Override
	public Optional<ProductCatalogue> get(UUID id) {
		return Optional.empty();
	}

	@Override
	public void cache(ProductCatalogue entity) {

	}

	@Override
	public void evict(UUID id) {

	}

}
