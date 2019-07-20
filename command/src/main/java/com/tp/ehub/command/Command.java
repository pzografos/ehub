package com.tp.ehub.command;

import java.util.function.Consumer;

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
		@Type(value = CreateProductCommand.class, name = CreateProductCommand.NAME),
		@Type(value = DeleteProductCommand.class, name = DeleteProductCommand.NAME),
		@Type(value = UpdateProductStockCommand.class, name = UpdateProductStockCommand.NAME),

		@Type(value = CreateOrderCommand.class, name = CreateOrderCommand.NAME), 
		@Type(value = CancelOrderCommand.class, name = CancelOrderCommand.NAME), 
		@Type(value = CompleteOrderCommand.class, name = CompleteOrderCommand.NAME)
})
public interface Command extends Message<String> {
	
	void consume(ConsumerVisitor mapper);
	
	public interface ConsumerVisitor extends Consumer<Command> {

		@Override
		default void accept(Command command) {
			command.consume(this);
		}

		default void visit(CreateProductCommand command) {fallback(command);}
		default void visit(DeleteProductCommand command) {fallback(command);}
		default void visit(UpdateProductStockCommand command) {fallback(command);}
		default void visit(CreateOrderCommand command) {fallback(command);}
		default void visit(CancelOrderCommand command) {fallback(command);}
		default void visit(CompleteOrderCommand command) {fallback(command);}

		default void fallback(Command command) {
			//do nothing
		}
	}

}
