package com.tp.ehub.product.service.messaging;

import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.order.model.event.OrderEvent;
import com.tp.ehub.product.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class OrderEventHandler implements Consumer<OrderEvent> {

	@Inject
	MessageReceiver receiver;

	@Inject
	ProductService service;

	public void run(Scheduler productScheduler) {
		
		final Flux<OrderEvent> eventsFlux = receiver.receiveAll(OrderEvent.class, new MessageReceiverOptions("product_order_event_receiver_v1.0", true)) 
				.map(MessageRecord::getMessage)
				.subscribeOn(productScheduler);
		
		eventsFlux.subscribe(this);
	}

	@Override
	public void accept(OrderEvent orderEvent) {
		// TODO Auto-generated method stub

	}

}
