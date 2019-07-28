package com.tp.ehub.common.views.function;

import com.tp.ehub.common.views.model.ProductCatalogueView;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductDeleted;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.model.ProductStatus;

public class ProductCatalogueViewReducerImpl implements ProductCatalogueViewReducer{

	@Override
	public ProductCatalogueView visit(ProductCatalogueView view, ProductCreated event) {
		Product product = new Product();
		product.setProductId(event.getProductId());
		product.setStatus(ProductStatus.CREATED);
		product.setCode(event.getCode());
		view.getProducts().put(product.getProductId(), product);
		return view;
	}

	@Override
	public ProductCatalogueView visit(ProductCatalogueView view, ProductDeleted event) {
		view.getProducts().get(event.getProductId()).setStatus(ProductStatus.DELETED);
		return view;
	}

	@Override
	public ProductCatalogueView visit(ProductCatalogueView view, ProductStockUpdated event) {
		view.getProducts().get(event.getProductId()).setQuantity(event.getQuantity());
		return view;
	}

}
