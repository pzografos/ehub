package com.tp.ehub.product.function;

import java.util.UUID;

import com.tp.ehub.common.domain.messaging.function.AggregateReducer;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductDeleted;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductCatalogueAggregate;
import com.tp.ehub.product.model.ProductStatus;

public class ProductCatalogueAggregateEventReducer implements AggregateReducer<UUID, ProductEvent, ProductCatalogue, ProductCatalogueAggregate>, ProductEvent.ReducerVisitor<ProductCatalogueAggregate>{

	@Override
	public ProductCatalogueAggregate visit(ProductCatalogueAggregate aggregate, ProductCreated event) {
		
		ProductCatalogue productCatalogue = aggregate.getRoot();

		Product product = new Product();
		product.setProductId(event.getProductId());
		product.setStatus(ProductStatus.CREATED);
		product.setCode(event.getCode());
		productCatalogue.getProducts().put(product.getProductId(), product);
		
		
		aggregate.setRootEntity(productCatalogue);
		aggregate.addEvent(event);
		
		return aggregate;
	}

	@Override
	public ProductCatalogueAggregate visit(ProductCatalogueAggregate aggregate, ProductDeleted event) {

		ProductCatalogue productCatalogue = aggregate.getRoot();

		productCatalogue.getProducts().get(event.getProductId()).setStatus(ProductStatus.DELETED);
		
		aggregate.setRootEntity(productCatalogue);
		aggregate.addEvent(event);
		
		return aggregate;
	}

	@Override
	public ProductCatalogueAggregate visit(ProductCatalogueAggregate aggregate, ProductStockUpdated event) {
		
		ProductCatalogue productCatalogue = aggregate.getRoot();

		productCatalogue.getProducts().get(event.getProductId()).setQuantity(event.getQuantity());

		aggregate.setRootEntity(productCatalogue);
		aggregate.addEvent(event);
		
		return aggregate;
	}

}
