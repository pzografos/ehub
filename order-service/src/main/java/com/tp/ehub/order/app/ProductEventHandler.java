package com.tp.ehub.order.app;

import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.order.service.OrderService;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class ProductEventHandler implements Consumer<ProductEvent> {

	@Inject
	MessageReceiver receiver;

	@Inject
	OrderService orderService;

	public void run(Scheduler productScheduler) {

		final Flux<ProductEvent> eventsFlux = receiver.receiveAll(ProductEvent.class, new MessageReceiverOptions("order_product_event_receiver_v1.0", true))
				.map(MessageRecord::getMessage)
				.subscribeOn(productScheduler);

		eventsFlux.subscribe(this);
	}

	@Override
	public void accept(ProductEvent event) {
		String e = event.getEventName();
		switch (e) {
		case ProductCreated.NAME:
			ProductCreated created = (ProductCreated) event;
			orderService.updateProductStock(created.getCompanyId(), created.getProductId(), created.getQuantity());
			break;
		case ProductStockUpdated.NAME:
			ProductStockUpdated stockUpdated = (ProductStockUpdated) event;
			orderService.updateProductStock(stockUpdated.getCompanyId(), stockUpdated.getProductId(), stockUpdated.getQuantity());
			break;
		default:
			return;
		}
	}

}
