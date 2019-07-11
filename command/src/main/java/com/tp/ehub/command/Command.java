package com.tp.ehub.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.infra.serialization.JsonMessage;

/**
 * The <code>Command</code> represents a trigger for the system. In its most
 * common form, it represents a user request.
 *
 */
@JsonMessage
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
		@Type(value = CreateOrderCommand.class, name = "Commands.CreateOrder"), 
		@Type(value = CreateProductCommand.class, name = "Commands.CreateProduct"),
		@Type(value = DeleteProductCommand.class, name = "Commands.DeleteProduct") })
public interface Command extends Message {

	String getKey();

	String getCommandName();

}
