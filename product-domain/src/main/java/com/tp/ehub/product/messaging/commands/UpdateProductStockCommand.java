package com.tp.ehub.product.messaging.commands;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to update the stock of an existing product
 *
 */
@JsonTypeName(UpdateProductStockCommand.NAME)
public class UpdateProductStockCommand extends ProductCommand {

	public static final String NAME = "Commands.UpdateProductStock";

	private Long quantity;

	public UpdateProductStockCommand() {

	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public void accept(ConsumerVisitor visitor){
		visitor.visit(this);
	}
}
