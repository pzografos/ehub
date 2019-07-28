package com.tp.ehub.order.service;

import java.util.UUID;

import com.tp.ehub.common.infra.service.AbstractCommandProcessor;
import com.tp.ehub.order.messaging.command.OrderCommand;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.model.OrderAggregate;

public class CommandProcessor extends AbstractCommandProcessor<UUID, OrderCommand, UUID, OrderEvent, Order, OrderAggregate> {

	public CommandProcessor() {
		super("order_command_receiver_v1.0", OrderCommand.class);
	}

	@Override
	protected String getAggregatePartitionKey(OrderCommand command) {
		return command.getCompanyId().toString();
	}

	@Override
	protected UUID getAggregateKey(OrderCommand command) {
		return command.getCompanyId();
	}

}
