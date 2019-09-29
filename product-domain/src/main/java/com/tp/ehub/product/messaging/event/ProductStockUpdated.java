package com.tp.ehub.product.messaging.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(ProductStockUpdated.NAME)
public class ProductStockUpdated extends ProductEvent {

	public static final String NAME = "Product.StockUpdated";

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

	@Override
	public <P, R> R map(P parameter, BiFunctionVisitor<P, R> visitor) {
		return visitor.visit(parameter, this);
	}

}
