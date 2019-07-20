package com.tp.ehub.product.service;

import java.util.Map;
import java.util.UUID;

import com.tp.ehub.product.model.Product;

public interface ProductService {

	void createProduct(UUID companyId, Product product);

	void deleteProduct(UUID companyId, UUID productId);
	
	void updateProductQuantity(UUID companyId, UUID productId, Long quantity);
	
	void addQuantities(UUID companyId, Map<UUID, Long> productQuantities);
	
	void removeQuantities(UUID companyId, Map<UUID, Long> productQuantities);
}
