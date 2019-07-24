package com.tp.ehub.order.messaging;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.tp.ehub.common.domain.messaging.container.AbstractMessageContainer;
import com.tp.ehub.order.messaging.command.OrderCommand;

@ApplicationScoped
@Named("order-commands")
public class OrderCommandsMessageContainer extends AbstractMessageContainer<UUID, OrderCommand> {

	public OrderCommandsMessageContainer() {
		super("order-commands", UUID.class, OrderCommand.class);
	}
}