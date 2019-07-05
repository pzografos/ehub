package com.tp.ehub.order.app;

import com.tp.ehub.order.service.messaging.CommandHandler;
import com.tp.ehub.order.service.messaging.ProductEventHandler;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class Main {

	public static void main(String[] args) {

		Scheduler scheduler = Schedulers.newParallel("order-service-scheduler", 2);

		SeContainer container = SeContainerInitializer.newInstance().initialize();

		CommandHandler commandHandler = container.select(CommandHandler.class).get();
		ProductEventHandler productEventHandler = container.select(ProductEventHandler.class).get();

		commandHandler.run(scheduler);
		productEventHandler.run(scheduler);

	}
}
