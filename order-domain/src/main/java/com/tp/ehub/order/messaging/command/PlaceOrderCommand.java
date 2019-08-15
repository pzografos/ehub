package com.tp.ehub.order.messaging.command;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tp.ehub.common.domain.exception.BusinessException;

/**
 * A <code>Command</code> to place a new order
 *
 */
@JsonTypeName(PlaceOrderCommand.NAME)
public class PlaceOrderCommand extends OrderCommand {

	public static final String NAME = "Commands.PlaceOrder";
	
	private Map<UUID, Long> basket;

	public PlaceOrderCommand() {

	}

	public Map<UUID, Long> getBasket() {
		return basket;
	}

	public void setBasket(Map<UUID, Long> basket) {
		this.basket = basket;
	}

	@Override
	public <P, R> R map(P parameter, BiFunctionVisitor<P, R> visitor) throws BusinessException{
		return visitor.visit(parameter, this);
	}
}
