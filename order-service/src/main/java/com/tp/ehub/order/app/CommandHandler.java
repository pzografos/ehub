package com.tp.ehub.order.app;

import com.tp.ehub.command.CancelOrderCommand;
import com.tp.ehub.command.Command;
import com.tp.ehub.command.CompleteOrderCommand;
import com.tp.ehub.command.CreateOrderCommand;
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.service.OrderService;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import javax.inject.Inject;
import java.util.UUID;
import java.util.function.Consumer;

public class CommandHandler implements Consumer<Command> {

	@Inject
	MessageReceiver commandsReceiver;

	@Inject
	OrderService service;

	public void run(Scheduler productScheduler) {

		final Flux<Command> commandsFlux = commandsReceiver.receiveAll(Command.class).map(MessageRecord::getMessage).subscribeOn(productScheduler);
		commandsFlux.subscribe(this);
	}

	@Override
	public void accept(Command command) {
		String c = command.getCommandName();
		switch (c) {
		case CreateOrderCommand.NAME:
			CreateOrderCommand create = (CreateOrderCommand) command;
			Order order = new Order();
			order.setBasket(create.getBasket());
			order.setId(UUID.randomUUID());
			order.setTimestamp(create.getTimestamp());
			service.placeOrder(order);
			break;
		case CancelOrderCommand.NAME:
			CancelOrderCommand cancel = (CancelOrderCommand) command;
			service.cancelOrder(cancel.getCompanyId(), cancel.getOrderId());
			break;
		case CompleteOrderCommand.NAME:
			CompleteOrderCommand complete = (CompleteOrderCommand) command;
			service.cancelOrder(complete.getCompanyId(), complete.getOrderId());
			break;
		default:
			return;
		}
	}

}
