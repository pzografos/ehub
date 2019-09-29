package com.tp.ehub.order.messaging.command;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to complete an existing order
 *
 */
@JsonTypeName(CompleteOrderCommand.NAME)
public class CompleteOrderCommand extends OrderCommand {

	public static final String NAME = "Commands.CompleteOrder";

	public CompleteOrderCommand() {

	}

	@Override
	public void accept(ConsumerVisitor visitor){
		visitor.visit(this);
	}
}
