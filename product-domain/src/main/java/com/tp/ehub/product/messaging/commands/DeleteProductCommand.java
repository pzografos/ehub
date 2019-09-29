package com.tp.ehub.product.messaging.commands;

import com.fasterxml.jackson.annotation.JsonTypeName;

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
	public void accept(ConsumerVisitor visitor){
		visitor.visit(this);
	}
}
