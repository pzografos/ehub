package com.tp.ehub.product.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductCreated extends ProductEvent {

	public static final String NAME = "PRODUCT_CREATED";

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
}
