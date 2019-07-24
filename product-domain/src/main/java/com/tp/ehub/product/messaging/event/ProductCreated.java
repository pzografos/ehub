package com.tp.ehub.product.messaging.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(ProductCreated.NAME)
public class ProductCreated extends ProductEvent {

	public static final String NAME = "Product.Created";

	private String code;

	private String name;

	private String description;

	private Long quantity;

	public ProductCreated() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
