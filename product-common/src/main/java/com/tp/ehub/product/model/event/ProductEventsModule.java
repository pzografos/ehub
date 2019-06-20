package com.tp.ehub.product.model.event;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Registers the types that may be found in the <code>ProductEventsTopic</code>. To
 * be used by <code>JacksonMapper</code>.
 *
 */
class ProductEventsModule extends SimpleModule{
	
	ProductEventsModule() {
		super();

		registerSubtypes(ProductCreated.class);
		registerSubtypes(ProductDeleted.class);
	}
}
