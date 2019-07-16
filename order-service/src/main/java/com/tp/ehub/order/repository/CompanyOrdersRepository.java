package com.tp.ehub.order.repository;

import java.util.UUID;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.tp.ehub.common.infra.repository.AbstractAggregateRepository;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.CompanyOrders;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.model.aggregate.CompanyOrdersAggregate;

@Dependent
public class CompanyOrdersRepository extends AbstractAggregateRepository<CompanyOrdersAggregate, OrderEvent, CompanyOrders, UUID> {

	@Inject
	ProductEventsStore productEventsStore;
	
	public CompanyOrdersRepository() {
		super();
	}

	@Override
	protected CompanyOrdersAggregate create(CompanyOrders root) {
		CompanyOrdersAggregate aggregate =  new CompanyOrdersAggregate(root);
		aggregate = populateStock(aggregate);
		return aggregate;
	}

	@Override
	protected CompanyOrders create(UUID id) {
		return new CompanyOrders(id);
	}
	
	public CompanyOrdersAggregate populateStock(CompanyOrdersAggregate companyOrdersAggregate){
		productEventsStore.getbyKey(companyOrdersAggregate.getRoot().getId())
			.forEach(event -> {
				String e = event.getEventName();
				switch (e) {
				case ProductCreated.NAME:
					ProductCreated created = (ProductCreated) event;
					companyOrdersAggregate.updateStock(created.getProductId(), created.getQuantity());
					break;
				case ProductStockUpdated.NAME:
					ProductStockUpdated stockUpdated = (ProductStockUpdated) event;
					companyOrdersAggregate.updateStock(stockUpdated.getProductId(), stockUpdated.getQuantity());
					break;
				default:
					return;
				}
			});
		return companyOrdersAggregate;
	}

}