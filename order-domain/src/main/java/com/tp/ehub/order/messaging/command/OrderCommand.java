package com.tp.ehub.order.messaging.command;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.exception.BusinessException;
import com.tp.ehub.common.domain.function.CheckedBiFunction;
import com.tp.ehub.common.domain.messaging.AbstractCommand;
import com.tp.ehub.common.domain.messaging.JsonMessage;
import com.tp.ehub.common.domain.messaging.container.Container;

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
@Container( name = "order-commands", keyClass = UUID.class)
public abstract class OrderCommand extends AbstractCommand<UUID>{

	protected UUID orderId;

	protected UUID companyId;
	
	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	@Override
	@JsonIgnore
	public UUID getKey() {
		return orderId;
	}

	protected abstract <P, R> R map(P parameter, BiFunctionVisitor<P, R> mapper) throws BusinessException;
	
	public interface BiFunctionVisitor<P, R> extends CheckedBiFunction<P, OrderCommand, R> {

		@Override
		default R apply(P parameter, OrderCommand command) throws BusinessException{
			return command.map(parameter, this);
		}

		default R visit(P parameter, PlaceOrderCommand command) throws BusinessException {return fallback(parameter, command);}
		default R visit(P parameter, CancelOrderCommand command) throws BusinessException {return fallback(parameter, command);}
		default R visit(P parameter, CompleteOrderCommand command) throws BusinessException {return fallback(parameter, command);}

		R fallback(P parameter, OrderCommand command) throws BusinessException;
	}
}
