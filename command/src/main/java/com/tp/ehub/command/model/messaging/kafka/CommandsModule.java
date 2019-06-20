package com.tp.ehub.command.model.messaging.kafka;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tp.ehub.command.CreateOrderCommand;
import com.tp.ehub.command.CreateProductCommand;
import com.tp.ehub.command.DeleteProductCommand;

/**
 * Registers the types that may be found in the <code>CommandsTopic</code>. To
 * be used by <code>JacksonMapper</code>.
 *
 */
class CommandsModule extends SimpleModule{
	
	CommandsModule() {
		super();

		registerSubtypes(CreateOrderCommand.class);
		registerSubtypes(CreateProductCommand.class);
		registerSubtypes(DeleteProductCommand.class);
	}
}
