package com.tp.ehub.product.app;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.ehub.product.service.messaging.CommandHandler;
import com.tp.ehub.product.service.messaging.OrderEventHandler;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		Scheduler productScheduler = Schedulers.newParallel("product-service-scheduler", 2);

		SeContainer container = SeContainerInitializer.newInstance().initialize();
		
		try {
			// TODO: remove when docker compose is fixed
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CommandHandler commandHandler = container.select(CommandHandler.class).get();
		OrderEventHandler orderEventHandler = container.select(OrderEventHandler.class).get();

		LOGGER.info("Product Service starting ... ");

		commandHandler.run(productScheduler);
		orderEventHandler.run(productScheduler);

	}
}
