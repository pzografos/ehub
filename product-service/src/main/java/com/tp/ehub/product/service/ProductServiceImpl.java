package com.tp.ehub.product.service;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.common.domain.exception.BusinessException;
import com.tp.ehub.common.domain.messaging.function.AggregateReducer;
import com.tp.ehub.common.domain.repository.AggregateRepository;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductDeleted;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

public class ProductServiceImpl implements ProductService{

	@Inject
	AggregateRepository<UUID, ProductEvent, ProductCatalogue, ProductCatalogueAggregate> repository;
	
	@Inject
	AggregateReducer<UUID, ProductEvent, ProductCatalogue, ProductCatalogueAggregate> aggregateEventReducer;
	
	@Override
	public Product create(UUID companyId, Product product) throws BusinessException {
		ProductCatalogueAggregate aggregate = repository.get(companyId);
		boolean createProductAllowed = aggregate.getRoot().getProducts().values().stream().noneMatch(p -> p.getCode().equals(product.getCode()));
		if (createProductAllowed) {
			UUID productId = randomUUID();
			ProductCreated productCreated = new ProductCreated();
			productCreated.setProductId(productId);
			productCreated.setCode(product.getCode());
			productCreated.setName(product.getName());
			productCreated.setDescription(product.getDescription());
			productCreated.setQuantity(product.getQuantity());
			productCreated.setCompanyId(companyId);
			productCreated.setTimestamp(ZonedDateTime.now());		
			aggregate = aggregateEventReducer.apply(aggregate, productCreated);
			repository.save(aggregate);		
			return aggregate.getRoot().getProducts().get(productId);
		} else {
			throw new BusinessException(format("There is already a product with code %s for company %s", product.getCode(), companyId));
		}
	}

	@Override
	public void delete(UUID companyId, UUID productId) throws BusinessException {
		ProductCatalogueAggregate aggregate = repository.get(companyId);
		boolean deleteProductAllowed = aggregate.getRoot().getProducts().containsKey(productId);
		if (deleteProductAllowed) {
			ProductDeleted productDeleted = new ProductDeleted();
			productDeleted.setProductId(productDeleted.getProductId());
			productDeleted.setTimestamp(ZonedDateTime.now());		
			aggregate = aggregateEventReducer.apply(aggregate, productDeleted);
			repository.save(aggregate);
		} else {
			throw new BusinessException(format("Product %s does not exist for company %s", productId, companyId));
		}
	}

	@Override
	public Product updateStock(UUID companyId, UUID productId, long quantity) throws BusinessException {
		ProductCatalogueAggregate aggregate = repository.get(companyId);
		ProductStockUpdated productStockUpdated = new ProductStockUpdated();
		productStockUpdated.setCompanyId(companyId);
		productStockUpdated.setProductId(productId);
		productStockUpdated.setQuantity(quantity);
		productStockUpdated.setTimestamp(ZonedDateTime.now());
		aggregate = aggregateEventReducer.apply(aggregate, productStockUpdated);
		repository.save(aggregate);
		return aggregate.getRoot().getProducts().get(productId);
	}

}
