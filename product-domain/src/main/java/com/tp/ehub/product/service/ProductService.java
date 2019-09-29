package com.tp.ehub.product.service;

import java.util.UUID;

import com.tp.ehub.common.domain.exception.BusinessException;
import com.tp.ehub.product.model.Product;

public interface ProductService {

	public Product create(UUID companyId, Product product) throws BusinessException;
	
	public void delete(UUID companyId, UUID productId) throws BusinessException;

	public Product updateStock(UUID companyId, UUID productId, long quantity) throws BusinessException;

}
