package com.tp.ehub.product.domain.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.UUID;

import com.tp.ehub.commands.product.CreateProduct;
import com.tp.ehub.commands.product.DeleteProduct;
import com.tp.ehub.common.model.Command;
import com.tp.ehub.common.service.AbstractCommandHandler;
import com.tp.ehub.product.domain.event.ProductCreated;
import com.tp.ehub.product.domain.event.ProductDeleted;
import com.tp.ehub.product.domain.event.ProductEvent;
import com.tp.ehub.product.domain.model.CompanyCatalogue;

public class CompanyProductsCommandHandler extends AbstractCommandHandler<ProductEvent, CompanyCatalogue, UUID> {

	@Override
	public Collection<ProductEvent> events(CompanyCatalogue rootEntity, Command command) {
		//TODO replace with visitor pattern
		if (command.getClass().isInstance(CreateProduct.class)) {
			CreateProduct createProduct = (CreateProduct) command; 
			if (rootEntity.getProducts().values().stream().noneMatch(p -> p.getCode().equals(createProduct.code()))) {
				ProductCreated event = new ProductCreated();
				event.product(UUID.randomUUID());
				event.company(createProduct.company());
				event.code(createProduct.code());
				return singletonList(event);
			}
		} else if (command.getClass().isInstance(DeleteProduct.class)) {
			DeleteProduct deleteProduct = (DeleteProduct) command; 
			if (rootEntity.getProducts().containsKey(deleteProduct.product())){
				ProductDeleted event = new ProductDeleted();
				event.product(UUID.randomUUID());
				return singletonList(event);
			}
		} 
		return emptyList();
	}

	@Override
	public UUID aggregateKey(com.tp.ehub.common.model.Command command) {
		return command.company();
	}

}
