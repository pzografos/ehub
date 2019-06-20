package com.tp.ehub.order.service.messaging;

import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.command.CreateOrderCommand;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.service.OrderService;

public class CommandHandler implements Consumer<Command>{

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
}
