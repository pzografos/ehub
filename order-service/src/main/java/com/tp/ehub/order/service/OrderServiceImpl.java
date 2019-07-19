package com.tp.ehub.order.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.common.domain.repository.AggregateRepository;
import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCompleted;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.CompanyOrders;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.model.aggregate.CompanyOrdersAggregate;

public class OrderServiceImpl implements OrderService {

	@Inject
	AggregateRepository<CompanyOrdersAggregate, OrderEvent, CompanyOrders, UUID> aggregateRepository;
	
	@Override
	public Order placeOrder(Order order) {		
		
		CompanyOrdersAggregate aggregate = aggregateRepository.get(order.getCompanyId());
		
		boolean placeOrderAllowed = order.getBasket().entrySet().stream()
				.noneMatch( basketItem -> aggregate.getStock().get(basketItem.getKey()) < basketItem.getValue());
				
		if (placeOrderAllowed) {
			OrderCreated orderCreated = new OrderCreated(order.getId());
			orderCreated.setCompanyId(order.getCompanyId());
			orderCreated.setBasket(order.getBasket());
			orderCreated.setTimestamp(ZonedDateTime.now());
			aggregate.apply(orderCreated);
		}
		return order;
	}
	
	@Override
	public void cancelOrder(UUID companyId, UUID orderId) {
		
		CompanyOrdersAggregate aggregate = aggregateRepository.get(companyId);
		Order order = aggregate.getRoot().getOrders().get(orderId);
		
		OrderCancelled orderCancelled = new OrderCancelled(companyId);
		orderCancelled.setOrderId(orderId);
		orderCancelled.setBasket(order.getBasket());
		orderCancelled.setTimestamp(ZonedDateTime.now());
		aggregate.apply(orderCancelled);		
	}
	
	@Override
	public void completeOrder(UUID companyId, UUID orderId) {
		
		CompanyOrdersAggregate aggregate = aggregateRepository.get(companyId);
		Order order = aggregate.getRoot().getOrders().get(orderId);

		OrderCompleted orderCompleted = new OrderCompleted(companyId);
		orderCompleted.setOrderId(orderId);
		orderCompleted.setBasket(order.getBasket());
		orderCompleted.setTimestamp(ZonedDateTime.now());
		aggregate.apply(orderCompleted);	
	}
}
