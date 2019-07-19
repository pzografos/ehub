package com.tp.ehub.product.service;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.common.domain.repository.AggregateRepository;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductDeleted;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.aggregate.ProductCatalogueAggregate;

public class ProductServiceImpl implements ProductService {

	@Inject
	AggregateRepository<ProductCatalogueAggregate, ProductEvent, ProductCatalogue, UUID> aggregateRepository;

	@Override
	public void createProduct(UUID companyId, Product product) {
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

	@Override
	public void deleteProduct(UUID companyId, UUID productId) {
		ProductCatalogueAggregate aggregate = aggregateRepository.get(companyId);
		boolean deleteProductAllowed = aggregate.getRoot().getProducts().containsKey(productId);
		if (deleteProductAllowed) {
			ProductDeleted productDeleted = new ProductDeleted();
			productDeleted.setProductId(productDeleted.getProductId());
			aggregate.apply(productDeleted);
			aggregateRepository.save(aggregate);
		}
	}

	@Override
	public void addQuantities(UUID companyId, Map<UUID, Long> productQuantities) {
		ProductCatalogueAggregate aggregate = aggregateRepository.get(companyId);
		productQuantities.entrySet().stream().forEach(entry -> {
			UUID productId = entry.getKey();
			Long quantity = entry.getValue();
			Long updatedQuantity = aggregate.getRoot().getProducts().get(productId).getQuantity() + quantity;
			ProductStockUpdated productStockUpdated = new ProductStockUpdated();
			productStockUpdated.setCompanyId(companyId);
			productStockUpdated.setProductId(productId);
			productStockUpdated.setQuantity(updatedQuantity);
			productStockUpdated.setTimestamp(ZonedDateTime.now());
			aggregate.apply(productStockUpdated);
		});
		aggregateRepository.save(aggregate);
	}

	@Override
	public void removeQuantities(UUID companyId, Map<UUID, Long> productQuantities) {
		ProductCatalogueAggregate aggregate = aggregateRepository.get(companyId);
		productQuantities.entrySet().stream().forEach(entry -> {
			UUID productId = entry.getKey();
			Long quantity = entry.getValue();
			Long updatedQuantity = aggregate.getRoot().getProducts().get(productId).getQuantity() - quantity;
			ProductStockUpdated productStockUpdated = new ProductStockUpdated();
			productStockUpdated.setCompanyId(companyId);
			productStockUpdated.setProductId(productId);
			productStockUpdated.setQuantity(updatedQuantity);
			productStockUpdated.setTimestamp(ZonedDateTime.now());
			aggregate.apply(productStockUpdated);
		});
		aggregateRepository.save(aggregate);

	}

}
