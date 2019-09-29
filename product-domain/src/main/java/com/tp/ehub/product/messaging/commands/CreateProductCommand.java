package com.tp.ehub.product.messaging.commands;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to create a new product
 *
 */
@JsonTypeName(CreateProductCommand.NAME)
public class CreateProductCommand extends ProductCommand {

	public static final String NAME = "Commands.CreateProduct";

	private String code;

	private String name;

	private String description;

	private Long quantity;

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
	public void accept(ConsumerVisitor visitor){
		visitor.visit(this);
	}
}
