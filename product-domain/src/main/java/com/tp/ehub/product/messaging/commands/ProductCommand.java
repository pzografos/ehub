package com.tp.ehub.product.messaging.commands;

import java.util.UUID;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.messaging.Command;
import com.tp.ehub.common.domain.messaging.JsonMessage;

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
		@Type(value = UpdateProductStockCommand.class, name = UpdateProductStockCommand.NAME)
})
public interface ProductCommand extends Command<UUID> {
		
	public UUID getCompanyId();
	
	<P, R> R map(P parameter, BiFunctionVisitor<P, R> mapper);
	
	public interface BiFunctionVisitor<P, R> extends BiFunction<P, ProductCommand, R> {

		@Override
		default R apply(P parameter, ProductCommand command) {
			return command.map(parameter, this);
		}

		default R visit(P parameter, CreateProductCommand command) {return fallback(parameter, command);}
		default R visit(P parameter, DeleteProductCommand command) {return fallback(parameter, command);}
		default R visit(P parameter, UpdateProductStockCommand command) {return fallback(parameter, command);}

		R fallback(P parameter, ProductCommand command);
	}
}
