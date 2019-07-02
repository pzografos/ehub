package com.tp.ehub.product.repository;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.repository.EntityCache;

/**
 * Temp implementation
 * 
 * TODO: replace by Redis implementation
 *
 */
@Dependent
public class ProductCatalogueEntityCache implements EntityCache<UUID, ProductCatalogue>{

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