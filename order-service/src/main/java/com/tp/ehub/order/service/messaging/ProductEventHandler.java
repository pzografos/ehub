package com.tp.ehub.order.service.messaging;

import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.order.service.OrderService;
import com.tp.ehub.product.model.event.ProductEvent;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@ApplicationScoped
public class ProductEventHandler implements Consumer<ProductEvent> {

	@Inject
	Logger log;

	@Inject
	MessageReceiver receiver;

	@Inject
	OrderService orderService;

	@Override
	public void accept(ProductEvent productEvent) {

		log.info(productEvent.getCompanyId().toString());

	}

	public void run(Scheduler scheduler) {
		
		final Flux<ProductEvent> eventsFlux = receiver.receiveAll(ProductEvent.class, new MessageReceiverOptions("product_event_receiver_v1.0", true)) 
				.map(MessageRecord::getMessage)
				.subscribeOn(scheduler);
		
		eventsFlux.subscribe(this);		
	}
}
