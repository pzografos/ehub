package com.tp.ehub.product.app;

import java.util.UUID;

import com.tp.ehub.common.infra.messaging.kafka.AbstractMessageProcessor;
import com.tp.ehub.order.messaging.event.OrderEvent;

public class OrderEventProcessor extends AbstractMessageProcessor<UUID, OrderEvent> {
	
	public OrderEventProcessor() {
		super("product_service_order_event_processor_v1.0", OrderEvent.class);
	}

	@Override
	public void process(OrderEvent message) {
		// TODO Auto-generated method stub
		
	}
	
//
//	@Override
//	protected String getAggregatePartitionKey(OrderEvent event) {
//		return event.getCompanyId().toString();
//	}
//
//	@Override
//	protected UUID getAggregateKey(OrderEvent event) {
//		return event.getCompanyId();
//	}

}
