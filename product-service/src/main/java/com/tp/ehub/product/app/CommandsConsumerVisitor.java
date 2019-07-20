package com.tp.ehub.product.app;

import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.command.CreateProductCommand;
import com.tp.ehub.command.DeleteProductCommand;
import com.tp.ehub.command.UpdateProductStockCommand;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.service.ProductService;

public class CommandsConsumerVisitor implements Command.ConsumerVisitor{

	@Inject
	ProductService service;

	@Override
	public void visit(CreateProductCommand command) {
		Product product = new Product();
		product.setProductId(UUID.randomUUID());
		product.setCode(command.getCode());
		product.setName(command.getName());
		product.setDescription(command.getDescription());
		product.setQuantity(command.getQuantity());
		service.createProduct(command.getCompanyId(), product);
	}

	@Override
	public void visit(DeleteProductCommand command) {
		service.deleteProduct(command.getCompanyId(), command.getProductId());
	}

	@Override
	public void visit(UpdateProductStockCommand command) {
		service.updateProductQuantity(command.getCompanyId(), command.getProductId(), command.getQuantity());
	}
}
