package com.tp.ehub.product.app;

import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.exception.BusinessException;
import com.tp.ehub.common.infra.messaging.kafka.AbstractMessageProcessor;
import com.tp.ehub.common.infra.request.RequestHandler;
import com.tp.ehub.product.messaging.commands.CreateProductCommand;
import com.tp.ehub.product.messaging.commands.DeleteProductCommand;
import com.tp.ehub.product.messaging.commands.ProductCommand;
import com.tp.ehub.product.messaging.commands.UpdateProductStockCommand;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.service.ProductService;

public class ProductCommandProcessor extends AbstractMessageProcessor<UUID, ProductCommand> implements ProductCommand.ConsumerVisitor{
	
	@Inject 
	ProductService productService;
	
	@Inject
	RequestHandler requestHandler;
	
	@Inject
	Logger log;
	
	public ProductCommandProcessor() {
		super("product_service_command_processor_v1.0", ProductCommand.class);
	}

	@Override
	public void process(ProductCommand command) {
		log.info("Processing command {}", command);
		try {
			command.accept(this);
			requestHandler.acceptRequest(command.getRequestId());
		} catch (BusinessException e) {
			requestHandler.failRequest(command.getRequestId(), e.getMessage());
		}
	}

	@Override
	public void visit(CreateProductCommand command) throws BusinessException {
		Product product = new Product();
		product.setCode(command.getCode());
		product.setName(command.getName());
		product.setDescription(command.getDescription());
		product.setQuantity(command.getQuantity());
		productService.create(command.getCompanyId(), product);
	}

	@Override
	public void visit(DeleteProductCommand command) throws BusinessException {
		productService.delete(command.getCompanyId(), command.getProductId());
	}

	@Override
	public void visit(UpdateProductStockCommand command) throws BusinessException {
		productService.updateStock(command.getCompanyId(), command.getProductId(), command.getQuantity());
	}
	
}
