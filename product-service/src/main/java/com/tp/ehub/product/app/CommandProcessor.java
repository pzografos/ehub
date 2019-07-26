package com.tp.ehub.product.app;

import java.util.Collection;
import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.infra.messaging.kafka.receiver.KafkaTopicReceiver;
import com.tp.ehub.product.messaging.commands.ProductCommand;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogueAggregate;
import com.tp.ehub.product.repository.ProductCatalogueRepository;
import com.tp.ehub.product.service.ProductCommandHandler;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class CommandProcessor implements Consumer<ProductCommand> {

	@Inject
	KafkaTopicReceiver receiver;
	
	@Inject
	ProductCatalogueRepository productCatalogueRepository;
	
	@Inject
	ProductCommandHandler productCommandHandler;

	public void run(Scheduler productScheduler) {
		
		receiver.setConsumerId("product_command_receiver_v1.0");
		
		final Flux<ProductCommand> commandsFlux = receiver.receiveAll(ProductCommand.class) 
				.map(MessageRecord::getMessage)
				.subscribeOn(productScheduler);
		commandsFlux.subscribe(this);
	}

	@Override
	public void accept(ProductCommand command) {
		ProductCatalogueAggregate aggregate = productCatalogueRepository.get(command.getCompanyId(), command.getCompanyId().toString());
		Collection<ProductEvent> events = productCommandHandler.apply(aggregate, command);
		events.forEach(aggregate::apply);
		productCatalogueRepository.save(aggregate);
	}

}
