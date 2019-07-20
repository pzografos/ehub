package com.tp.ehub.product.app;

import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.order.messaging.event.OrderEvent;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class OrderEventHandler implements Consumer<OrderEvent> {

	@Inject
	MessageReceiver receiver;
	
	@Inject
	OrderEventConsumerVisitor consumer;

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
		consumer.accept(event);
	}

}
