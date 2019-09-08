package com.tp.ehub.common.views.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tp.ehub.common.domain.model.View;
import com.tp.ehub.product.model.Product;

public class ProductCatalogueView implements View<UUID> {
	
	private UUID companyId;

	private Map<UUID, Product> products = new HashMap<UUID, Product>();
	
	public ProductCatalogueView() {
		
	}
	
	public ProductCatalogueView(UUID companyId) {
		this.companyId = companyId;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setProducts(Map<UUID, Product> products) {
		this.products = products;
	}

	public Map<UUID, Product> getProducts() {
		return products;
	}

	@Override
	public UUID getKey() {
		return companyId;
	}
}
