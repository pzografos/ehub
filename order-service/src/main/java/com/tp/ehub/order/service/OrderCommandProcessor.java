package com.tp.ehub.order.service;

import java.util.UUID;

import com.tp.ehub.common.infra.messaging.kafka.AbstractMessageProcessor;
import com.tp.ehub.order.messaging.command.OrderCommand;

public class OrderCommandProcessor extends AbstractMessageProcessor<UUID, OrderCommand> {

	public OrderCommandProcessor() {
		super("order_command_receiver_v1.0", OrderCommand.class);
	}

	@Override
	public void process(OrderCommand message) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	protected String getAggregatePartitionKey(OrderCommand command) {
//		return command.getCompanyId().toString();
//	}
//
//	@Override
//	protected UUID getAggregateKey(OrderCommand command) {
//		return command.getCompanyId();
//	}

}
