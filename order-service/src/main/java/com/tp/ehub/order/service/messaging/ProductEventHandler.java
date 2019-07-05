package com.tp.ehub.order.service.messaging;

import com.tp.ehub.command.Command;
import com.tp.ehub.messaging.kafka.service.ConsumerGroupKafkaReceiver;
import com.tp.ehub.messaging.kafka.service.Receiver;
import com.tp.ehub.messaging.kafka.service.SinglePartitionKafkaReceiver;
import com.tp.ehub.model.messaging.MessageRecord;
import com.tp.ehub.order.service.OrderService;
import com.tp.ehub.product.model.event.ProductEvent;
import com.tp.ehub.service.messaging.GlobalMessageReceiver;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
import java.util.function.Consumer;

@ApplicationScoped
public class ProductEventHandler implements Consumer<ProductEvent> {

	@Inject
	Logger log;

	@Inject
	@Receiver("product-events")
	ConsumerGroupKafkaReceiver<UUID, ProductEvent> productEventsReceiver;

	@Inject
	OrderService orderService;

	@Override
	public void accept(ProductEvent productEvent) {

		log.info(productEvent.getCompanyId().toString());

	}

	public void run(Scheduler scheduler) {
		final Flux<ProductEvent> commandsFlux = productEventsReceiver.receive("product_event_receiver_v1.0", true).map(MessageRecord::getMessage).subscribeOn(scheduler);
		commandsFlux.subscribe(this);
	}
}
