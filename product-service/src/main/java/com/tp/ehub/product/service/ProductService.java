package com.tp.ehub.product.service;

import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.aggregate.ProductCatalogueAggregate;
import com.tp.ehub.product.model.event.ProductCreated;
import com.tp.ehub.product.model.event.ProductDeleted;
import com.tp.ehub.product.model.event.ProductEvent;
import com.tp.ehub.repository.AggregateRepository;

public class ProductService {

	@Inject
	AggregateRepository<ProductCatalogueAggregate, ProductEvent, ProductCatalogue, UUID> aggregateRepository;

	public void createProduct(Product product, UUID companyId) {
		ProductCatalogueAggregate aggregate = aggregateRepository.get(companyId);
		boolean createProductAllowed = aggregate.getRoot().getProducts().values().stream().noneMatch(p -> p.getCode().equals(product.getCode()));
		if (createProductAllowed) {
			ProductCreated productCreated = new ProductCreated();
			productCreated.setProductId(product.getId());
			productCreated.setCode(product.getCode());
			productCreated.setName(product.getName());
			productCreated.setDescription(product.getDescription());
			productCreated.setQuantity(product.getQuantity());
			productCreated.setCompanyId(companyId);
			aggregate.apply(productCreated);
			aggregateRepository.save(aggregate);
		}
	}

	public void deleteProduct(UUID productId, UUID companyId) {
		ProductCatalogueAggregate aggregate = aggregateRepository.get(companyId);
		boolean deleteProductAllowed = aggregate.getRoot().getProducts().containsKey(productId);
		if (deleteProductAllowed) {
			ProductDeleted productDeleted = new ProductDeleted();
			productDeleted.setProductId(productDeleted.getProductId());
			aggregate.apply(productDeleted);
			aggregateRepository.save(aggregate);
		}
	}

}
