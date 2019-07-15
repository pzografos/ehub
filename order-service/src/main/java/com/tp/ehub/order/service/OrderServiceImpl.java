package com.tp.ehub.order.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.common.domain.repository.AggregateRepository;
import com.tp.ehub.order.messaging.event.OrderCreated;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.CompanyOrders;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.product.model.aggregate.CompanyOrdersAggregate;

public class OrderServiceImpl implements OrderService {

	@Inject
	AggregateRepository<CompanyOrdersAggregate, OrderEvent, CompanyOrders, UUID> aggregateRepository;
	
	@Override
	public Order placeOrder(Order order) {		
		
		CompanyOrdersAggregate aggregate = aggregateRepository.get(order.getId());
		
		boolean placeOrderAllowed = true; //TODO: get information from productService
		
		if (placeOrderAllowed) {
			OrderCreated orderCreated = new OrderCreated(order.getId());
			orderCreated.setCompanyId(order.getCompanyId());
			orderCreated.setTimestamp(ZonedDateTime.now());
			aggregate.apply(orderCreated);
		}
		return order;
	}
	
	@Override
	public void cancelOrder(UUID orderID) {
		
	}
	
	@Override
	public void completeOrder(UUID orderID) {
		
	}
}
