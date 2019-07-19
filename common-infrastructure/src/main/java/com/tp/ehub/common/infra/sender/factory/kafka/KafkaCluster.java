package com.tp.ehub.common.infra.sender.factory.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.tp.ehub.common.infra.qualifier.EhubProperty;

/**
 * Provides all the environment-specific details of a Kafka cluster installation
 */
@ApplicationScoped
public class KafkaCluster {

	@Inject
	@EhubProperty("KAFKA_BROKERS")
	String brokers;

	public String getBrokers() {
		return brokers;
	}

}
