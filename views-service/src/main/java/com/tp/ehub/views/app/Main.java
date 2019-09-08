package com.tp.ehub.views.app;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import com.tp.ehub.views.service.ProductCatalogueViewProcessor;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Main {

	public static void main(String[] args) {

		Scheduler scheduler = Schedulers.newParallel("view-service-scheduler", 1);

		SeContainer container = SeContainerInitializer.newInstance().initialize();

		ProductCatalogueViewProcessor productCatalogueViewProcessor = container.select(ProductCatalogueViewProcessor.class).get();

		productCatalogueViewProcessor.run(scheduler);
	}

}
