package com.tp.ehub.product.service;

import java.util.Map;
import java.util.UUID;

import com.tp.ehub.product.model.Product;

public interface ProductService {

	public void createProduct(UUID companyId, Product product);

	public void deleteProduct(UUID companyId, UUID productId);
	
	public void addQuantities(UUID companyId, Map<UUID, Long> productQuantities);
	
	public void removeQuantities(UUID companyId, Map<UUID, Long> productQuantities);
}
