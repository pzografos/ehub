package com.tp.ehub.product.service;

import static java.util.Collections.emptyList;

import java.util.Collection;

import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

public interface OrderEventHandler extends OrderEvent.BiFunctionVisitor<ProductCatalogueAggregate, Collection<ProductEvent>>{

	@Override
	default public Collection<ProductEvent> fallback(ProductCatalogueAggregate parameter, OrderEvent event) {
		return emptyList();
	}
}
