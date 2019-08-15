package com.tp.ehub.order.messaging.command;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tp.ehub.common.domain.exception.BusinessException;

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
	public <P, R> R map(P parameter, BiFunctionVisitor<P, R> visitor) throws BusinessException{
		return visitor.visit(parameter, this);
	}
}
