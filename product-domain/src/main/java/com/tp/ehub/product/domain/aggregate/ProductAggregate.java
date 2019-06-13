package com.tp.ehub.product.domain.aggregate;

import java.util.UUID;

import com.tp.ehub.common.service.AbstractAggregate;
import com.tp.ehub.product.domain.event.ProductCreated;
import com.tp.ehub.product.domain.event.ProductDeleted;
import com.tp.ehub.product.domain.event.ProductEvent;
import com.tp.ehub.product.domain.model.Product;
import com.tp.ehub.product.domain.model.ProductStatus;

public class ProductAggregate extends AbstractAggregate<ProductEvent, Product, UUID> {

	@Override
	protected Product mutate(Product entity, ProductEvent event) {
		//TODO replace with visitor pattern
		if (event.getClass().isInstance(ProductDeleted.class)) {
			entity.setStatus(ProductStatus.DELETED);
		} else if (event.getClass().isInstance(ProductCreated.class)) {
			entity.setStatus(ProductStatus.CREATED);
			entity.setCode(((ProductCreated)event).code());
		} 
		return entity;
	}

	@Override
	protected Product createRoot(UUID id) {
		return new Product(id);
	}
	
}
