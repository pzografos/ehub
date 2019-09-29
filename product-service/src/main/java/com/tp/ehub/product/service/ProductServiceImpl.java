package com.tp.ehub.product.service;

import com.tp.ehub.product.model.Product;

public class ProductServiceImpl implements ProductService{

	@Override
	public Product create(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Product updateStock(Product product, long stock) {
		// TODO Auto-generated method stub
		return null;
	}


//	@Override
//	protected String getAggregatePartitionKey(ProductCommand command) {
//		return command.getCompanyId().toString();
//	}
//
//	@Override
//	protected UUID getAggregateKey(ProductCommand command) {
//		return command.getCompanyId();
//	}

//
//	@Override
//	protected String getAggregatePartitionKey(OrderEvent event) {
//		return event.getCompanyId().toString();
//	}
//
//	@Override
//	protected UUID getAggregateKey(OrderEvent event) {
//		return event.getCompanyId();
//	}
	
}
