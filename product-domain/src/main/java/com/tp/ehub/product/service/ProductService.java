package com.tp.ehub.product.service;

import java.util.UUID;

import com.tp.ehub.product.model.Product;

public interface ProductService {

	public void createProduct(Product product, UUID companyId);

	public void deleteProduct(UUID productId, UUID companyId);
}
