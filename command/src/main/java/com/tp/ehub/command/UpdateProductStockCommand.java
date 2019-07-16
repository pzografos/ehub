package com.tp.ehub.command;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to update the stock of an existing product
 *
 */
@JsonTypeName("Commands.UpdateProductStock")
public class UpdateProductStockCommand implements Command {

	public static final String NAME = "UPDATE_PRODUCT_STOCK";

	private UUID productId;

	private Long quantity;

	private ZonedDateTime timestamp;

	private UUID companyId;

	public UpdateProductStockCommand() {

	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	@Override
	@JsonIgnore
	public String getCommandName() {
		return NAME;
	}

	@Override
	@JsonIgnore
	public String getKey() {
		return companyId.toString();
	}
}
