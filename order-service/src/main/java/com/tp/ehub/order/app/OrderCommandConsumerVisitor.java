package com.tp.ehub.order.app;

import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.command.CancelOrderCommand;
import com.tp.ehub.command.Command;
import com.tp.ehub.command.CompleteOrderCommand;
import com.tp.ehub.command.CreateOrderCommand;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.service.OrderService;

public class OrderCommandConsumerVisitor implements Command.ConsumerVisitor{

	@Inject
	OrderService service;

	@Override
	public void visit(CreateOrderCommand command) {
		Order order = new Order();
		order.setBasket(command.getBasket());
		order.setId(UUID.randomUUID());
		order.setTimestamp(command.getTimestamp());
		service.placeOrder(order);
	}

	@Override
	public void visit(CancelOrderCommand command) {
		service.cancelOrder(command.getCompanyId(), command.getOrderId());
	}

	@Override
	public void visit(CompleteOrderCommand command) {
		service.cancelOrder(command.getCompanyId(), command.getOrderId());
	}
	
}
