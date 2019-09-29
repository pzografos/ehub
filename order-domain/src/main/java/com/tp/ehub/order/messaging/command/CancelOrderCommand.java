package com.tp.ehub.order.messaging.command;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to cancel an existing order
 *
 */
@JsonTypeName(CancelOrderCommand.NAME)
public class CancelOrderCommand extends OrderCommand {

	public static final String NAME = "Commands.CancelOrder";

	public CancelOrderCommand() {
		
	}

	@Override
	public void accept(ConsumerVisitor visitor){
		visitor.visit(this);
	}
}
