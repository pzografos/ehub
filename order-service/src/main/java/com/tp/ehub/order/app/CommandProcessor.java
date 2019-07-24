package com.tp.ehub.order.app;

import java.util.Collection;
import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.order.messaging.command.OrderCommand;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.OrderAggregate;
import com.tp.ehub.order.repository.OrderRepository;
import com.tp.ehub.order.service.OrderCommandHandler;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class CommandProcessor implements Consumer<OrderCommand> {

	@Inject
	MessageReceiver receiver;
	
	@Inject
	OrderRepository orderRepository;
	
	@Inject
	OrderCommandHandler orderCommandHandler;

	public void run(Scheduler productScheduler) {
		MessageReceiverOptions options = new MessageReceiverOptions();
		options.setConsumerId("order_command_receiver_v1.0");
		options.setFromStart(true);
		final Flux<OrderCommand> commandsFlux = receiver.receiveAll(OrderCommand.class, options)
				.filter(record -> record.getMessage().getClass().isAssignableFrom(OrderCommand.class))
				.map(MessageRecord::getMessage)
				.subscribeOn(productScheduler);
		commandsFlux.subscribe(this);
	}

	@Override
	public void accept(OrderCommand command) {
		OrderAggregate aggregate = orderRepository.get(command.getOrderId());
		Collection<OrderEvent> events = orderCommandHandler.apply(aggregate, command);
		events.forEach(aggregate::apply);
		orderRepository.save(aggregate);
	}

}
