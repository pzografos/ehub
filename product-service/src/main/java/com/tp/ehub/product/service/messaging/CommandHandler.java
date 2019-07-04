package com.tp.ehub.product.service.messaging;

import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.command.CreateProductCommand;
import com.tp.ehub.command.DeleteProductCommand;
import com.tp.ehub.messaging.kafka.service.Receiver;
import com.tp.ehub.model.messaging.MessageRecord;
import com.tp.ehub.product.model.Product;
import com.tp.ehub.product.service.ProductService;
import com.tp.ehub.service.messaging.GlobalMessageReceiver;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class CommandHandler implements Consumer<Command>{

	@Inject
	@Receiver("commands-receiver")
	private GlobalMessageReceiver<String, Command> commandsReceiver;
	
	@Inject
	private ProductService service;
	
	public void run(Scheduler productScheduler) {
		final Flux<Command> commandsFlux = commandsReceiver.receive("product_command_receiver_v1.0", true)
				.map(MessageRecord::getMessage)
				.subscribeOn(productScheduler)
				;
		commandsFlux.subscribe(this);
	}

	@Override
	public void accept(Command command) {
		String c = command.getCommandName();
		switch (c) {
		case CreateProductCommand.NAME:
			CreateProductCommand cpc = (CreateProductCommand) command;
			Product product = new Product();
			product.setProductId(UUID.randomUUID());
			product.setCode(cpc.getCode());
			product.setName(cpc.getName());
			product.setDescription(cpc.getDescription());
			product.setQuantity(cpc.getQuantity());
			service.createProduct(product, cpc.getCompanyId());
			break;
		case DeleteProductCommand.NAME:
			DeleteProductCommand dpc = (DeleteProductCommand) command;
			service.deleteProduct(dpc.getProductId(), dpc.getCompanyId());
			break;
		default:
			return;
		}
	}

}
