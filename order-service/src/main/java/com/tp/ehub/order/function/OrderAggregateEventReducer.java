package com.tp.ehub.order.function;

import static com.tp.ehub.order.model.OrderStatus.CANCELLED;
import static com.tp.ehub.order.model.OrderStatus.COMPLETED;
import static com.tp.ehub.order.model.OrderStatus.CREATED;

import java.util.UUID;

import com.tp.ehub.common.domain.messaging.function.AggregateReducer;
import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCompleted;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.model.OrderAggregate;

public class OrderAggregateEventReducer implements AggregateReducer<UUID, OrderEvent, Order, OrderAggregate>, OrderEvent.ReducerVisitor<OrderAggregate>{

	@Override
	public OrderAggregate visit(OrderAggregate aggregate, OrderCreated event) {
		
		Order order = aggregate.getRoot();
		order.setCompanyId(event.getCompanyId());
		order.setId(event.getOrderId());
		order.setStatus(CREATED);
		order.setBasket(event.getBasket());
		
		aggregate.setRootEntity(order);
		aggregate.addEvent(event);
		
		return aggregate;
	}

	@Override
	public OrderAggregate visit(OrderAggregate aggregate, OrderCancelled event) {
		
		Order order = aggregate.getRoot();
		order.setStatus(CANCELLED);
		
		aggregate.setRootEntity(order);
		aggregate.addEvent(event);
		
		return aggregate;
	}

	@Override
	public OrderAggregate visit(OrderAggregate aggregate, OrderCompleted event) {

		Order order = aggregate.getRoot();
		order.setStatus(COMPLETED);
		
		aggregate.setRootEntity(order);
		aggregate.addEvent(event);
		
		return aggregate;
	}
}
