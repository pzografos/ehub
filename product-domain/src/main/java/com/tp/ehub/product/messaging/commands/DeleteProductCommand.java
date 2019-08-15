package com.tp.ehub.product.messaging.commands;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tp.ehub.common.domain.exception.BusinessException;

/**
 * A <code>Command</code> to delete an existing product
 *
 */
@JsonTypeName(DeleteProductCommand.NAME)
public class DeleteProductCommand extends ProductCommand {

	public static final String NAME = "Commands.DeleteProduct";

	public DeleteProductCommand() {

	}

	@Override
	public <P, R> R map(P parameter, BiFunctionVisitor<P, R> visitor) throws BusinessException {
		return visitor.visit(parameter, this);
	}
}
