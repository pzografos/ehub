package com.tp.ehub.order.model.aggregate;

import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCompleted;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.CompanyOrders;
import com.tp.ehub.order.model.Order;

public class CompanyOrderReducer implements OrderEvent.ReducerVisitor<CompanyOrders>{

	@Override
	public CompanyOrders visit(CompanyOrders companyOrders, OrderCreated event) {
		Order order = new Order();
		order.setCompanyId(event.getCompanyId());
		order.setId(event.getOrderId());
		order.setBasket(event.getBasket());
		companyOrders.getOrders().put(event.getOrderId(), order);
		return companyOrders;
	}

	@Override
	public CompanyOrders visit(CompanyOrders companyOrders, OrderCancelled event) {
		companyOrders.getOrders().remove(event.getOrderId());
		return companyOrders;
	}

	@Override
	public CompanyOrders visit(CompanyOrders companyOrders, OrderCompleted event) {
		companyOrders.getOrders().remove(event.getOrderId());
		return companyOrders;
	}

}
