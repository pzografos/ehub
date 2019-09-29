package com.tp.ehub.product.app;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.ehub.product.service.OrderEventProcessor;
import com.tp.ehub.product.service.ProductCommandProcessor;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		Scheduler productScheduler = Schedulers.newParallel("product-service-scheduler", 2);

		SeContainer container = SeContainerInitializer.newInstance().initialize();

		ProductCommandProcessor commandProcessor = container.select(ProductCommandProcessor.class).get();
		OrderEventProcessor orderEventProcessor = container.select(OrderEventProcessor.class).get();

		LOGGER.info("Product Service starting ... ");

		commandProcessor.run(productScheduler);
		orderEventProcessor.run(productScheduler);
	}
}
