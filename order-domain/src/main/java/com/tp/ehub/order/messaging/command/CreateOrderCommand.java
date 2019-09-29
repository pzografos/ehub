package com.tp.ehub.order.messaging.command;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A <code>Command</code> to place a new order
 *
 */
@JsonTypeName(CreateOrderCommand.NAME)
public class CreateOrderCommand extends OrderCommand {

	public static final String NAME = "Commands.CreateOrder";
	
	private Map<UUID, Long> basket;

	public CreateOrderCommand() {

	}

	public Map<UUID, Long> getBasket() {
		return basket;
	}

	public void setBasket(Map<UUID, Long> basket) {
		this.basket = basket;
	}

	@Override
	public void accept(ConsumerVisitor visitor){
		visitor.visit(this);
	}
}
