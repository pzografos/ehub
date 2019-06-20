package com.tp.ehub.messaging.kafka;

import java.io.Serializable;

/**
 * Provides all the environment-specific details of a Kafka cluster installation
 *
 */
public interface KafkaCluster extends Serializable{

	public String getBrokers();
}
