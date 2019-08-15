package com.tp.ehub.order.messaging.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tp.ehub.common.domain.exception.BusinessException;

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
	public <P, R> R map(P parameter, BiFunctionVisitor<P, R> visitor) throws BusinessException{
		return visitor.visit(parameter, this);
	}
}
