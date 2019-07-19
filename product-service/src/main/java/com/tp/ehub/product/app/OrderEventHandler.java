package com.tp.ehub.product.app;

import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.product.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class OrderEventHandler implements Consumer<OrderEvent> {

	@Inject
	MessageReceiver receiver;

	@Inject
	ProductService service;

	@Inject
	MessageReceiverOptions options;

	public void run(Scheduler productScheduler) {

		final Flux<OrderEvent> eventsFlux = receiver.receiveAll(OrderEvent.class, options).map(MessageRecord::getMessage).subscribeOn(productScheduler);

		eventsFlux.subscribe(this);
	}

	@Override
	public void accept(OrderEvent event) {
		String e = event.getEventName();
		switch (e) {
		case OrderCreated.NAME:
			OrderCreated created = (OrderCreated) event;
			service.removeQuantities(created.getCompanyId(), created.getBasket());
			break;
		case OrderCancelled.NAME:
			OrderCancelled cancelled = (OrderCancelled) event;
			service.addQuantities(cancelled.getCompanyId(), cancelled.getBasket());
			break;
		default:
			return;
		}
	}

}
