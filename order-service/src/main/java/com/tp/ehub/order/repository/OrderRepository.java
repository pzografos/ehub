package com.tp.ehub.order.repository;

import static com.tp.ehub.order.model.OrderStatus.NONE;

import java.util.UUID;

import javax.inject.Inject;

import com.tp.ehub.common.infra.repository.AbstractPartitionedAggregateRepository;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.Order;
import com.tp.ehub.order.model.OrderAggregate;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;

public class OrderRepository extends AbstractPartitionedAggregateRepository<OrderAggregate, OrderEvent, Order, UUID> {

	@Inject
	ProductEventsStore productEventsStore;
	
	public OrderRepository() {
		super();
	}

	@Override
	protected OrderAggregate create(Order root) {
		OrderAggregate aggregate =  new OrderAggregate(root);
		aggregate = populateStock(aggregate);
		return aggregate;
	}

	@Override
	protected Order create(UUID id) {
		Order order = new Order();
		order.setId(id);
		order.setStatus(NONE);
		return order;
	}
	
	public OrderAggregate populateStock(OrderAggregate companyOrdersAggregate){	
		productEventsStore.getbyKey(companyOrdersAggregate.getRoot().getId())
			.forEach(event -> {	
				if (event.getClass().isInstance(ProductCreated.class)) {
					ProductCreated created = (ProductCreated) event;
					companyOrdersAggregate.updateStock(created.getProductId(), created.getQuantity());
				} else if (event.getClass().isInstance(ProductCreated.class)) {
					ProductStockUpdated stockUpdated = (ProductStockUpdated) event;
					companyOrdersAggregate.updateStock(stockUpdated.getProductId(), stockUpdated.getQuantity());
				}
			});
		return companyOrdersAggregate;
	}

}