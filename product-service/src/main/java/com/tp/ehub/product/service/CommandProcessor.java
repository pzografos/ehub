package com.tp.ehub.product.service;

import java.util.UUID;

import com.tp.ehub.common.infra.service.AbstractCommandProcessor;
import com.tp.ehub.product.messaging.commands.ProductCommand;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogue;
import com.tp.ehub.product.model.ProductCatalogueAggregate;

public class CommandProcessor extends AbstractCommandProcessor<UUID, ProductCommand, UUID, ProductEvent, ProductCatalogue, ProductCatalogueAggregate> {
	
	public CommandProcessor() {
		super("product_command_receiver_v1.0", ProductCommand.class);
	}

	@Override
	protected String getAggregatePartitionKey(ProductCommand command) {
		return command.getCompanyId().toString();
	}

	@Override
	protected UUID getAggregateKey(ProductCommand command) {
		return command.getCompanyId();
	}

}
