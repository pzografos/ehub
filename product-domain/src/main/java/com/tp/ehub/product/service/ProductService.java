package com.tp.ehub.product.service;

import com.tp.ehub.product.model.Product;

public interface ProductService {

	public Product create(Product product);
	
	public void delete(Product product);

	public Product updateStock(Product product, long stock);

}
