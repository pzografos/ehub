package com.tp.ehub.product.service;

import java.util.UUID;

import com.tp.ehub.common.infra.messaging.kafka.AbstractMessageProcessor;
import com.tp.ehub.product.messaging.commands.ProductCommand;

public class ProductCommandProcessor extends AbstractMessageProcessor<UUID, ProductCommand> {
	
	public ProductCommandProcessor() {
		super("product_service_command_processor_v1.0", ProductCommand.class);
	}

	@Override
	public void process(ProductCommand message) {
		// TODO Auto-generated method stub
		
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

}
