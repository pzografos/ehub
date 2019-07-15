package com.tp.ehub.product.model.aggregate;

import java.util.UUID;

import com.tp.ehub.common.infra.model.AbstractAggregate;
import com.tp.ehub.order.messaging.event.OrderEvent;
import com.tp.ehub.order.model.CompanyOrders;

public class CompanyOrdersAggregate extends AbstractAggregate<OrderEvent, CompanyOrders, UUID> {

	public CompanyOrdersAggregate(CompanyOrders rootEntity) {
		super(rootEntity);
	}

	@Override
	protected CompanyOrders mutate(CompanyOrders entity, OrderEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

}
