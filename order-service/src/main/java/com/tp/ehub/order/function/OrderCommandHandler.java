package com.tp.ehub.order.function;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

import com.tp.ehub.common.domain.exception.BusinessException;
import com.tp.ehub.common.domain.messaging.function.CommandHandler;
import com.tp.ehub.order.messaging.command.CancelOrderCommand;
import com.tp.ehub.order.messaging.command.CompleteOrderCommand;
import com.tp.ehub.order.messaging.command.OrderCommand;
import com.tp.ehub.order.messaging.command.CreateOrderCommand;
import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCompleted;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.model.OrderAggregate;

public class OrderCommandHandler
		implements CommandHandler<UUID, OrderCommand, UUID, OrderEvent, Order, OrderAggregate>, OrderCommand.BiFunctionVisitor<OrderAggregate, Collection<OrderEvent>> {

	@Override
	public Collection<OrderEvent> visit(OrderAggregate aggregate, CreateOrderCommand command) throws BusinessException {

		boolean placeOrderAllowed = command.getBasket().entrySet().stream().noneMatch(basketItem -> aggregate.getStock().get(basketItem.getKey()) < basketItem.getValue());

		if (placeOrderAllowed) {
			OrderCreated orderCreated = new OrderCreated(command.getOrderId());
			orderCreated.setCompanyId(command.getCompanyId());
			orderCreated.setBasket(command.getBasket());
			orderCreated.setTimestamp(ZonedDateTime.now());
			return singleton(orderCreated);
		} else {
			throw new BusinessException(format("Not enough stock"));
		}
	}

	@Override
	public Collection<OrderEvent> visit(OrderAggregate aggregate, CancelOrderCommand command) throws BusinessException {

		Order order = aggregate.getRoot();

		OrderCancelled orderCancelled = new OrderCancelled(command.getOrderId());
		orderCancelled.setBasket(order.getBasket());
		orderCancelled.setTimestamp(ZonedDateTime.now());
		return singleton(orderCancelled);
	}

	@Override
	public Collection<OrderEvent> visit(OrderAggregate aggregate, CompleteOrderCommand command) throws BusinessException {

		Order order = aggregate.getRoot();

		OrderCompleted orderCompleted = new OrderCompleted(command.getOrderId());
		orderCompleted.setBasket(order.getBasket());
		orderCompleted.setTimestamp(ZonedDateTime.now());
		return singleton(orderCompleted);
	}

	@Override
	public Collection<OrderEvent> fallback(OrderAggregate parameter, OrderCommand command) throws BusinessException {
		return emptyList();
	}
}
