package com.tp.ehub.product.domain.aggregate;

import java.util.UUID;

import com.tp.ehub.common.service.AbstractAggregate;
import com.tp.ehub.product.domain.event.ProductCreated;
import com.tp.ehub.product.domain.event.ProductDeleted;
import com.tp.ehub.product.domain.event.ProductEvent;
import com.tp.ehub.product.domain.model.CompanyCatalogue;

public class CompanyProductAggregate extends AbstractAggregate<ProductEvent, CompanyCatalogue, UUID> {
	
	@Override
	protected CompanyCatalogue mutate(CompanyCatalogue entity, ProductEvent event) {
		//TODO replace with visitor pattern
		if (event.getClass().isInstance(ProductDeleted.class)) {
			entity.getCodes().remove(event.id());
		} else if (event.getClass().isInstance(ProductCreated.class)) {
			entity.getCodes().put(event.id(), ((ProductCreated)event).code());
		} 
		return entity;
	}

	@Override
	protected CompanyCatalogue createRoot(UUID id) {
		return new CompanyCatalogue(id);
	}

}
