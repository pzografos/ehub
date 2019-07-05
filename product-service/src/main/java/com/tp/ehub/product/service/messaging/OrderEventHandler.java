package com.tp.ehub.product.service.messaging;

import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.messaging.kafka.service.Receiver;
import com.tp.ehub.model.messaging.MessageRecord;
import com.tp.ehub.order.model.event.OrderEvent;
import com.tp.ehub.product.service.ProductService;
import com.tp.ehub.service.messaging.GlobalMessageReceiver;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class OrderEventHandler implements Consumer<OrderEvent> {

	@Inject
	@Receiver("order-events")
	GlobalMessageReceiver<UUID, OrderEvent> orderEventsReceiver;

	@Inject
	ProductService service;

	public void run(Scheduler productScheduler) {
		final Flux<OrderEvent> eventsFlux = orderEventsReceiver.receive("product_order_event_receiver_v1.0", true).map(MessageRecord::getMessage).subscribeOn(productScheduler);
		eventsFlux.subscribe(this);
	}

	@Override
	public void accept(OrderEvent orderEvent) {
		// TODO Auto-generated method stub

	}

}
