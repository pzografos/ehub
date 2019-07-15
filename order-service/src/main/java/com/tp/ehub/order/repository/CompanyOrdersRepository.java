package com.tp.ehub.order.repository;

import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.common.infra.repository.AbstractAggregateRepository;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.CompanyOrders;
import com.tp.ehub.product.model.aggregate.CompanyOrdersAggregate;

@Dependent
public class CompanyOrdersRepository extends AbstractAggregateRepository<CompanyOrdersAggregate, OrderEvent, CompanyOrders, UUID> {

	public CompanyOrdersRepository() {
		super();
	}

	@Override
	protected CompanyOrdersAggregate create(CompanyOrders root) {
		return new CompanyOrdersAggregate(root);
	}

	@Override
	protected CompanyOrders create(UUID id) {
		return new CompanyOrders(id);
	}

}