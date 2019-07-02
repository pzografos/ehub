package com.tp.ehub.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.model.messaging.Message;
import com.tp.ehub.serialization.JsonMessage;

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
