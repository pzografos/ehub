package com.tp.ehub.common.infra.producer;

import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Properties;

public class ProducerConfigFactory {

	@Inject
	KafkaCluster kafka;

	@Produces
	@ProducerConfiguration
	public Properties createProducerConfigProperties() {

		Properties result = new Properties();
		result.put(ProducerConfig.RETRIES_CONFIG, 10);
		result.put(ProducerConfig.ACKS_CONFIG, "all");
		result.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers());
		result.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		result.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
		return result;

	}
}
