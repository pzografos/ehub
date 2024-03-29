package com.tp.ehub.product.messaging.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(ProductDeleted.NAME)
public class ProductDeleted extends ProductEvent {

	public static final String NAME = "Product.Deleted";

	public ProductDeleted() {
		super();
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
