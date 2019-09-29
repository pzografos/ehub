package com.tp.ehub.order.app;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import com.tp.ehub.order.service.OrderCommandProcessor;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Main {

	public static void main(String[] args) {

		Scheduler scheduler = Schedulers.newParallel("order-service-scheduler", 1);

		SeContainer container = SeContainerInitializer.newInstance().initialize();

		OrderCommandProcessor commandHandler = container.select(OrderCommandProcessor.class).get();

		commandHandler.run(scheduler);

	}
}
