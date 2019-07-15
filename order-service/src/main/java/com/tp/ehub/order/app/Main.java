package com.tp.ehub.order.app;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Main {

	public static void main(String[] args) {

		Scheduler scheduler = Schedulers.newParallel("order-service-scheduler", 2);

		SeContainer container = SeContainerInitializer.newInstance().initialize();

		CommandHandler commandHandler = container.select(CommandHandler.class).get();

		commandHandler.run(scheduler);

	}
}
