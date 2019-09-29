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
	public void accept(ConsumerVisitor visitor) throws BusinessException {
		visitor.visit(this);
	}
}
