package com.tp.ehub.product.messaging.commands;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to delete an existing product
 *
 */
@JsonTypeName(DeleteProductCommand.NAME)
public class DeleteProductCommand implements ProductCommand {

	public static final String NAME = "Commands.DeleteProduct";

	private ZonedDateTime timestamp;

	private UUID productId;

	private UUID companyId;

	public DeleteProductCommand() {

	}

	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	@Override
	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	@Override
	@JsonIgnore
	public UUID getKey() {
		return companyId;
	}
	
	@Override
	public <P, R> R map(P parameter, BiFunctionVisitor<P, R> visitor) {
		return visitor.visit(parameter, this);
	}
}
