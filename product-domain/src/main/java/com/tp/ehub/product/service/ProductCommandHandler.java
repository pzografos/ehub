package com.tp.ehub.product.service;

import static java.util.Collections.emptyList;

import java.util.Collection;

import com.tp.ehub.product.messaging.commands.ProductCommand;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

public interface ProductCommandHandler extends ProductCommand.BiFunctionVisitor<ProductCatalogueAggregate, Collection<ProductEvent>>{

	@Override
	default public Collection<ProductEvent> fallback(ProductCatalogueAggregate parameter, ProductCommand command) {
		return emptyList();
	}
}
