package com.tp.ehub.product.function;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.function.CommandHandler;
import com.tp.ehub.product.messaging.commands.CreateProductCommand;
import com.tp.ehub.product.messaging.commands.DeleteProductCommand;
import com.tp.ehub.product.messaging.commands.ProductCommand;
import com.tp.ehub.product.messaging.commands.UpdateProductStockCommand;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductDeleted;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

public class ProductCommandHandler implements CommandHandler<UUID, ProductCommand, UUID, ProductEvent, ProductCatalogue, ProductCatalogueAggregate>, ProductCommand.BiFunctionVisitor<ProductCatalogueAggregate, Collection<ProductEvent>>{

	@Inject
	Logger log;
	
	@Override
	public Collection<ProductEvent> visit(ProductCatalogueAggregate aggregate, CreateProductCommand command) {
		boolean createProductAllowed = aggregate.getRoot().getProducts().values().stream().noneMatch(p -> p.getCode().equals(command.getCode()));
		if (createProductAllowed) {
			ProductCreated productCreated = new ProductCreated();
			productCreated.setProductId(command.getProductId());
			productCreated.setCode(command.getCode());
			productCreated.setName(command.getName());
			productCreated.setDescription(command.getDescription());
			productCreated.setQuantity(command.getQuantity());
			productCreated.setCompanyId(command.getCompanyId());
			return singleton(productCreated);
		}
		log.warn(format("Could not create product %s for company %s", command.getProductId(), command.getCompanyId()));
		return emptyList();
	}

	@Override
	public Collection<ProductEvent> visit(ProductCatalogueAggregate aggregate, DeleteProductCommand command) {
		boolean deleteProductAllowed = aggregate.getRoot().getProducts().containsKey(command.getProductId());
		if (deleteProductAllowed) {
			ProductDeleted productDeleted = new ProductDeleted();
			productDeleted.setProductId(productDeleted.getProductId());
			return singleton(productDeleted);
		}
		log.warn(format("Could not delete product %s for company %s", command.getProductId(), command.getCompanyId()));
		return emptyList();
	}

	@Override
	public Collection<ProductEvent> visit(ProductCatalogueAggregate aggregate, UpdateProductStockCommand command) {
		ProductStockUpdated productStockUpdated = new ProductStockUpdated();
		productStockUpdated.setCompanyId(command.getCompanyId());
		productStockUpdated.setProductId(command.getProductId());
		productStockUpdated.setQuantity(command.getQuantity());
		productStockUpdated.setTimestamp(ZonedDateTime.now());
		return singleton(productStockUpdated);	
	}
	
	@Override
	public Collection<ProductEvent> fallback(ProductCatalogueAggregate parameter, ProductCommand command) {
		return emptyList();
	}

}
