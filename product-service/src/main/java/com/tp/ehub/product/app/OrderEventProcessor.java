package com.tp.ehub.product.app;

import java.util.Collection;
import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.model.ProductCatalogueAggregate;
import com.tp.ehub.product.repository.ProductCatalogueRepository;
import com.tp.ehub.product.service.OrderEventHandler;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class OrderEventProcessor implements Consumer<OrderEvent> {

	@Inject
	MessageReceiver receiver;
	
	@Inject
	ProductCatalogueRepository productCatalogueRepository;
	
	@Inject
	OrderEventHandler orderEventHandler;

	public void run(Scheduler productScheduler) {
		
		MessageReceiverOptions options = new MessageReceiverOptions();
		options.setConsumerId("product_order_event_receiver_v1.0");
		options.setFromStart(true);
		
		final Flux<OrderEvent> eventsFlux = receiver.receiveAll(OrderEvent.class, options) 
				.map(MessageRecord::getMessage)
				.subscribeOn(productScheduler);
		
		eventsFlux.subscribe(this);
	}

	@Override
	public void accept(OrderEvent event) {
		ProductCatalogueAggregate aggregate = productCatalogueRepository.get(event.getCompanyId());
		Collection<ProductEvent> events = orderEventHandler.apply(aggregate, event);
		events.forEach(aggregate::apply);
		productCatalogueRepository.save(aggregate);
	}

}
