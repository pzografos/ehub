package com.tp.ehub.product.domain.model;

import java.util.UUID;

import com.tp.ehub.common.types.Entity;

public class Product implements Entity<UUID>{

	private UUID productId;
	
	private ProductStatus status;
	
	private String code;
	
	public Product(UUID productId) {
		this.productId = productId;
	}
	
	@Override
	public UUID id() {
		return productId;
	}

	public UUID getProductId() {
		return productId;
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

}
