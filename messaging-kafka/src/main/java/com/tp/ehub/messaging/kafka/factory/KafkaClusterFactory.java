package com.tp.ehub.messaging.kafka.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.tp.ehub.messaging.kafka.KafkaCluster;

@ApplicationScoped
public class KafkaClusterFactory {

	@Produces
	@ApplicationScoped
	public KafkaCluster getKafkaCluster() {
		return new KafkaClusterImpl(System.getenv("KAFKA_BROKERS"));
	}
}
