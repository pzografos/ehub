package com.tp.ehub.product.messaging;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tp.ehub.product.messaging.event.ProductCreated;
import com.tp.ehub.product.messaging.event.ProductDeleted;
import com.tp.ehub.product.messaging.event.ProductStockUpdated;

/**
 * Registers the types that may be found in the <code>ProductEventsTopic</code>.
 * To be used by <code>JacksonMapper</code>.
 *
 */
class ProductEventsModule extends SimpleModule {

	ProductEventsModule() {
		super();

		registerSubtypes(ProductCreated.class);
		registerSubtypes(ProductStockUpdated.class);
		registerSubtypes(ProductDeleted.class);
	}
}
