package com.tp.ehub.product.model;

import java.util.UUID;

import com.tp.ehub.common.domain.model.RootEntity;

public class Product implements RootEntity<UUID> {

	private UUID productId;

	private ProductStatus status;

	private String code;

	private String name;

	private String description;

	private Long quantity;

	public Product() {
	}

	@Override
	public UUID getId() {
		return productId;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
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

}
