package com.tp.ehub.product.model.aggregate;

import java.util.UUID;

import com.tp.ehub.common.infra.model.AbstractAggregate;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductStatus;
import com.tp.ehub.product.model.event.ProductCreated;
import com.tp.ehub.product.model.event.ProductDeleted;
import com.tp.ehub.product.model.event.ProductEvent;

public class ProductCatalogueAggregate extends AbstractAggregate<ProductEvent, ProductCatalogue, UUID> {

	public ProductCatalogueAggregate(ProductCatalogue rootEntity) {
		super(rootEntity);
	}

	@Override
	protected ProductCatalogue mutate(ProductCatalogue catalogue, ProductEvent event) {
		if (ProductDeleted.NAME.equals(event.getEventName())) {
			catalogue.getProducts().get(event.getProductId()).setStatus(ProductStatus.DELETED);
		} else if (ProductCreated.NAME.equals(event.getEventName())) {
			ProductCreated productCreated = (ProductCreated) event;
			Product product = new Product();
			product.setProductId(productCreated.getProductId());
			product.setStatus(ProductStatus.CREATED);
			product.setCode(productCreated.getCode());
			catalogue.getProducts().put(product.getProductId(), product);
		}
		return catalogue;
	}

}
