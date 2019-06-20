package com.tp.ehub.product.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
