package com.tp.ehub.command;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Commands.DeleteProduct")
public class DeleteProductCommand implements Command{

	public static final String NAME = "DELETE_PRODUCT";
	
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
