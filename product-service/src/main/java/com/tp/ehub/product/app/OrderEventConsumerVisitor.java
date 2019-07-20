package com.tp.ehub.product.app;

import javax.inject.Inject;

import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.product.service.ProductService;

public class OrderEventConsumerVisitor implements OrderEvent.ConsumerVisitor{

	@Inject
	ProductService service;
	
	@Override
	public void visit(OrderCreated event) {
		OrderCreated created = event;
		service.removeQuantities(created.getCompanyId(), created.getBasket());
	}

	@Override
	public void visit(OrderCancelled event) {
		OrderCancelled cancelled = event;
		service.addQuantities(cancelled.getCompanyId(), cancelled.getBasket());
	}

}
