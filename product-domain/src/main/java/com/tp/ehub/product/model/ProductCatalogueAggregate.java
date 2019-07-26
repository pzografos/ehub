package com.tp.ehub.product.model;

import java.util.UUID;

import com.tp.ehub.common.domain.model.AbstractAggregate;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductDeleted;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;

public class ProductCatalogueAggregate extends AbstractAggregate<ProductEvent, ProductCatalogue, UUID> {

	private Long version;

	public ProductCatalogueAggregate(ProductCatalogue rootEntity) {
		super(rootEntity);
	}
	
	@Override
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public void apply(ProductEvent event) {
		switch (event.getEventName()) {
		case ProductCreated.NAME:
			applyProductCreated((ProductCreated) event);
			break;
		case ProductDeleted.NAME:
			applyProductDeleted((ProductDeleted) event);
			break;
		case ProductStockUpdated.NAME:
			applyProductStockUpdated((ProductStockUpdated) event);
			break;
		default:
			throw new IllegalArgumentException(String.format("Unknown event %s", event.getEventName()));
		}	
		newEvents.add(event);
	}

	public void applyProductCreated(ProductCreated event) {
		ProductCreated productCreated = event;
		Product product = new Product();
		product.setProductId(productCreated.getProductId());
		product.setStatus(ProductStatus.CREATED);
		product.setCode(productCreated.getCode());
		rootEntity.getProducts().put(product.getProductId(), product);
	}

	public void applyProductDeleted(ProductDeleted event) {
		rootEntity.getProducts().get(event.getProductId()).setStatus(ProductStatus.DELETED);
	}

	public void applyProductStockUpdated(ProductStockUpdated event) {
		rootEntity.getProducts().get(event.getProductId()).setQuantity(event.getQuantity());
	}
}
