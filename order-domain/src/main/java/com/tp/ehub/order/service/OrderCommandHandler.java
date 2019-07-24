package com.tp.ehub.order.service;

import static java.util.Collections.emptyList;

import java.util.Collection;

import com.tp.ehub.order.messaging.command.OrderCommand;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.OrderAggregate;

public interface OrderCommandHandler extends OrderCommand.BiFunctionVisitor<OrderAggregate, Collection<OrderEvent>>{

	@Override
	default public Collection<OrderEvent> fallback(OrderAggregate parameter, OrderCommand command) {
		return emptyList();
	}
}
