package com.tp.ehub.messaging.kafka.factory;

import com.tp.ehub.messaging.kafka.KafkaCluster;

class KafkaClusterImpl implements KafkaCluster{

	private String brokers;

	public KafkaClusterImpl(String brokers) {
		this.brokers = brokers;
	}

	@Override
	public String getBrokers() {
		return brokers;
	}
	
}
