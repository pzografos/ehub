package com.tp.ehub.order.messaging.command;

import java.util.UUID;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.messaging.Command;
import com.tp.ehub.common.domain.messaging.JsonMessage;

/**
 * Represents a <code>Command</code> that refers to an order
 *
 */
@JsonMessage
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
		@Type(value = PlaceOrderCommand.class, name = PlaceOrderCommand.NAME), 
		@Type(value = CancelOrderCommand.class, name = CancelOrderCommand.NAME), 
		@Type(value = CompleteOrderCommand.class, name = CompleteOrderCommand.NAME)
})
public interface OrderCommand extends Command<UUID>{

	/**
	 * @return the order Id the <code>Command</code> refers to
	 */
	public UUID getOrderId();
	
	<P, R> R map(P parameter, BiFunctionVisitor<P, R> mapper);
	
	public interface BiFunctionVisitor<P, R> extends BiFunction<P, OrderCommand, R> {

		@Override
		default R apply(P parameter, OrderCommand command) {
			return command.map(parameter, this);
		}

		default R visit(P parameter, PlaceOrderCommand command) {return fallback(parameter, command);}
		default R visit(P parameter, CancelOrderCommand command) {return fallback(parameter, command);}
		default R visit(P parameter, CompleteOrderCommand command) {return fallback(parameter, command);}

		R fallback(P parameter, OrderCommand command);
	}
}
