package com.tp.ehub.order.messaging;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tp.ehub.order.messaging.event.OrderCancelled;
import com.tp.ehub.order.messaging.event.OrderCompleted;
import com.tp.ehub.order.messaging.event.OrderCreated;

/**
 * Registers the types that may be found in the <code>OrderEventsTopic</code>.
 * To be used by <code>JacksonMapper</code>.
 *
 */
class OrderEventsModule extends SimpleModule {

	OrderEventsModule() {
		super();

		registerSubtypes(OrderCreated.class);
		registerSubtypes(OrderCancelled.class);
		registerSubtypes(OrderCompleted.class);
	}
}