package com.tp.ehub.common.infra.producer;

import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Properties;

@ApplicationScoped
public class ConsumerConfigFactory {

	@Inject
	MessageReceiverOptions options;

	@Inject
	KafkaCluster kafka;

	@Produces
	@ConsumerConfiguration
	public Properties createConsumerConfigProperties() {

		Properties result = new Properties();
		result.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, String.valueOf(500L));
		result.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers());
		result.put(ConsumerConfig.GROUP_ID_CONFIG, options.getConsumerId());
		result.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		result.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
		return result;

	}

}
