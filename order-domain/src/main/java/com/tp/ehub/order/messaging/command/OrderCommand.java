package com.tp.ehub.order.messaging.command;

import java.util.UUID;
import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
		@Type(value = CreateOrderCommand.class, name = CreateOrderCommand.NAME), 
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


	protected abstract void accept(ConsumerVisitor mapper);
	
	public interface ConsumerVisitor extends Consumer<OrderCommand> {

		@Override
		default void accept(OrderCommand command){
			command.accept(this);
		}

		default void visit(CancelOrderCommand command) {fallback(command);}
		default void visit(CompleteOrderCommand command) {fallback(command);}
		default void visit(CreateOrderCommand command) {fallback(command);}

		void fallback(OrderCommand command);
	}
}
