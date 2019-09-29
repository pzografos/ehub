package com.tp.ehub.product.app;

import java.util.UUID;

import com.tp.ehub.common.infra.messaging.kafka.AbstractMessageProcessor;
import com.tp.ehub.product.messaging.commands.CreateProductCommand;
import com.tp.ehub.product.messaging.commands.DeleteProductCommand;
import com.tp.ehub.product.messaging.commands.ProductCommand;
import com.tp.ehub.product.messaging.commands.UpdateProductStockCommand;

public class ProductCommandProcessor extends AbstractMessageProcessor<UUID, ProductCommand> implements ProductCommand.ConsumerVisitor{
	
	public ProductCommandProcessor() {
		super("product_service_command_processor_v1.0", ProductCommand.class);
	}

	@Override
	public void process(ProductCommand command) {
		command.accept(this);
	}

	@Override
	public void visit(CreateProductCommand command) {

	}

	@Override
	public void visit(DeleteProductCommand command) {

	}

	@Override
	public void visit(UpdateProductStockCommand command) {

	}
	
}
