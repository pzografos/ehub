package com.tp.ehub.product.domain.aggregate;

import java.util.UUID;

import com.tp.ehub.common.service.AbstractAggregate;
import com.tp.ehub.product.domain.event.ProductCreated;
import com.tp.ehub.product.domain.event.ProductDeleted;
import com.tp.ehub.product.domain.event.ProductEvent;
import com.tp.ehub.product.domain.model.CompanyCatalogue;
import com.tp.ehub.product.domain.model.Product;
import com.tp.ehub.product.domain.model.ProductStatus;

public class CompanyProductAggregate extends AbstractAggregate<ProductEvent, CompanyCatalogue, UUID> {
	
	@Override
	protected CompanyCatalogue mutate(CompanyCatalogue entity, ProductEvent event) {
		//TODO replace with visitor pattern
		if (event.getClass().isInstance(ProductDeleted.class)) {
			entity.getProducts().get(event.product()).setStatus(ProductStatus.DELETED);
		} else if (event.getClass().isInstance(ProductCreated.class)) {
			ProductCreated productCreated = (ProductCreated) event;
			//TODO Check if another product with the same code exists in the company's products
			Product product = new Product(UUID.randomUUID());
			product.setStatus(ProductStatus.CREATED);
			product.setCode(productCreated.code());
			entity.getProducts().put(product.getProductId(), product);
		} 
		return entity;
	}

	@Override
	protected CompanyCatalogue createRoot(UUID id) {
		return new CompanyCatalogue(id);
	}

}
