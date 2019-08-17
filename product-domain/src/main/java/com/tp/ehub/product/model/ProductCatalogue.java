package com.tp.ehub.product.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tp.ehub.common.domain.model.Entity;

public class ProductCatalogue implements Entity {

	private UUID companyId;

	private Map<UUID, Product> products = new HashMap<UUID, Product>();
	
	public ProductCatalogue(UUID companyId) {
		this.companyId = companyId;
	}

	public UUID getId() {
		return companyId;
	}

	public Map<UUID, Product> getProducts() {
		return products;
	}

	@Override
	public String toString() {
		return "ProductCatalogue [companyId=" + companyId + ", products=" + products + "]";
	}
	
}