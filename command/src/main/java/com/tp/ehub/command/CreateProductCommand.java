package com.tp.ehub.command;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Commands.CreateProduct")
public class CreateProductCommand implements Command {

	public static final String NAME = "CREATE_PRODUCT";

	private String code;
	
	private String name;

	private String description;

	private Long quantity;
	
	private ZonedDateTime timestamp;
	
	private UUID companyId;

	public CreateProductCommand() {

	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
