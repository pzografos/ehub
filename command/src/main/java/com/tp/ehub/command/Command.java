package com.tp.ehub.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.messaging.JsonMessage;
import com.tp.ehub.common.domain.messaging.Message;

/**
 * The <code>Command</code> represents a trigger for the system. In its most
 * common form, it represents a user request.
 *
 */
@JsonMessage
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
		@Type(value = CreateProductCommand.class, name = "Commands.CreateProduct"),
		@Type(value = DeleteProductCommand.class, name = "Commands.DeleteProduct"),
		@Type(value = DeleteProductCommand.class, name = "Commands.UpdateProductStock"),

		@Type(value = CreateOrderCommand.class, name = "Commands.CreateOrder"), 
		@Type(value = CancelOrderCommand.class, name = "Commands.CancelOrder"), 
		@Type(value = CompleteOrderCommand.class, name = "Commands.CompleteOrder")
})
public interface Command extends Message<String> {

	@Override
	String getKey();

	String getCommandName();

}
