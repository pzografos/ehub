package com.tp.ehub.product.model.aggregate;

import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductDeleted;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductStatus;

class ProductCatalogueReducer implements ProductEvent.ReducerVisitor<ProductCatalogue>{

	@Override
	public ProductCatalogue visit(ProductCatalogue catalogue, ProductCreated event) {
		Product product = new Product();
		product.setProductId(event.getProductId());
		product.setStatus(ProductStatus.CREATED);
		product.setCode(event.getCode());
		catalogue.getProducts().put(product.getProductId(), product);
		return catalogue;
	}

	@Override
	public ProductCatalogue visit(ProductCatalogue catalogue, ProductDeleted event) {
		catalogue.getProducts().get(event.getProductId()).setStatus(ProductStatus.DELETED);
		return catalogue;
	}

	@Override
	public ProductCatalogue visit(ProductCatalogue catalogue, ProductStockUpdated event) {
		catalogue.getProducts().get(event.getProductId()).setQuantity(event.getQuantity());
		return catalogue;
	}

}
