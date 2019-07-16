package com.tp.ehub.product.model.aggregate;

import java.util.UUID;

import com.tp.ehub.common.infra.model.AbstractAggregate;
import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCompleted;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.messaging.event.StockChanged;
import com.tp.ehub.order.model.CompanyOrders;
import com.tp.ehub.order.model.Order;

public class CompanyOrdersAggregate extends AbstractAggregate<OrderEvent, CompanyOrders, UUID> {

	public CompanyOrdersAggregate(CompanyOrders rootEntity) {
		super(rootEntity);
	}

	@Override
	protected CompanyOrders mutate(CompanyOrders entity, OrderEvent event) {
		String e = event.getEventName();
		switch (e) {
		case OrderCreated.NAME:
			OrderCreated created = (OrderCreated) event;
			Order order = new Order();
			order.setCompanyId(created.getCompanyId());
			order.setId(created.getOrderId());
			order.setBasket(created.getBasket());
			entity.getOrders().put(created.getOrderId(), order);
			return entity;
		case OrderCancelled.NAME:
			OrderCancelled cancelled = (OrderCancelled) event;
			entity.getOrders().remove(cancelled.getOrderId());
			return entity;
		case OrderCompleted.NAME:
			OrderCompleted completed = (OrderCompleted) event;
			entity.getOrders().remove(completed.getOrderId());
			return entity;
		case StockChanged.NAME:
			StockChanged stockChanged = (StockChanged) event;
			entity.getStock().put(stockChanged.getProductId(), stockChanged.getQuantity());
			return entity;
		default:
			return entity;
		}
	}

}
