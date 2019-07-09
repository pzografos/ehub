package com.tp.ehub.order.service.messaging;

import java.util.UUID;
import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.command.CreateOrderCommand;
import com.tp.ehub.messaging.kafka.service.Receiver;
import com.tp.ehub.model.messaging.MessageRecord;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.service.OrderService;
import com.tp.ehub.service.messaging.GlobalMessageReceiver;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

@ApplicationScoped
public class CommandHandler implements Consumer<Command> {

	@Inject
	@Receiver("commands")
	GlobalMessageReceiver<String, Command> commandsReceiver;

	@Inject
	OrderService orderService;

	@Override
	public void accept(Command command) {
		String c = command.getCommandName();
		switch (c) {
		case CreateOrderCommand.NAME:
			CreateOrderCommand coc = (CreateOrderCommand) command;
			Order order = new Order();
			order.setId(UUID.randomUUID());
			order.setBasket(coc.getBasket());
			order.setTimestamp(coc.getTimestamp());
			orderService.placeOrder(new Order());
		default:
			return;
		}
	}

	public void run(Scheduler scheduler) {
		final Flux<Command> commandsFlux = commandsReceiver.receive("product_command_receiver_v1.0", true).map(MessageRecord::getMessage).subscribeOn(scheduler);
		commandsFlux.subscribe(this);
	}
}
