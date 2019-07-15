package com.tp.ehub.product.messaging.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Product.Deleted")
public class ProductDeleted extends ProductEvent {

	public static final String NAME = "PRODUCT_DELETED";

	public ProductDeleted() {
		super();
	}

	@Override
	@JsonIgnore
	public String getEventName() {
		return NAME;
	}
}
