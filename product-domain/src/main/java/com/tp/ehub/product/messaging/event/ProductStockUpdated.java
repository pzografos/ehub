package com.tp.ehub.product.messaging.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Product.StockUpdated")
public class ProductStockUpdated extends ProductEvent {

	public static final String NAME = "PRODUCT_STOCK_UPDATED";

	private Long quantity;

	public ProductStockUpdated() {
		super();
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	@JsonIgnore
	public String getEventName() {
		return NAME;
	}

}
