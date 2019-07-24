package com.tp.ehub.product.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

public class OrderEventHandlerImpl implements OrderEventHandler{

	@Override
	public Collection<ProductEvent> visit(ProductCatalogueAggregate aggregate, OrderCreated event) {
		Collection<ProductEvent> events = new ArrayList<>();
		event.getBasket().entrySet().stream().forEach(
				entry -> {
					UUID productId = entry.getKey();
					Long quantity = entry.getValue();
					Long updatedQuantity = aggregate.getRoot().getProducts().get(productId).getQuantity() + quantity;
					ProductStockUpdated productStockUpdated = new ProductStockUpdated();
					productStockUpdated.setCompanyId(event.getCompanyId());
					productStockUpdated.setProductId(productId);
					productStockUpdated.setQuantity(updatedQuantity);
					productStockUpdated.setTimestamp(ZonedDateTime.now());
					events.add(productStockUpdated);
				}
			);
		return events;
	}

	@Override
	public Collection<ProductEvent> visit(ProductCatalogueAggregate aggregate, OrderCancelled event) {
		Collection<ProductEvent> events = new ArrayList<>();
		event.getBasket().entrySet().stream().forEach(
				entry -> {
					UUID productId = entry.getKey();
					Long quantity = entry.getValue();
					Long updatedQuantity = aggregate.getRoot().getProducts().get(productId).getQuantity() - quantity;
					ProductStockUpdated productStockUpdated = new ProductStockUpdated();
					productStockUpdated.setCompanyId(event.getCompanyId());
					productStockUpdated.setProductId(productId);
					productStockUpdated.setQuantity(updatedQuantity);
					productStockUpdated.setTimestamp(ZonedDateTime.now());
					events.add(productStockUpdated);
				}
			);
		return events;
	}

}
