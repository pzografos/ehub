package com.tp.ehub.messaging.kafka;

import com.tp.ehub.common.infra.property.EhubProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;

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
