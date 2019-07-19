package com.tp.ehub.common.infra.sender.bean;

import com.tp.ehub.common.domain.messaging.sender.MessageSenderConfiguration;
import com.tp.ehub.common.infra.sender.factory.kafka.KafkaCluster;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Properties;

public class DefaultMessageSenderConfiguration implements MessageSenderConfiguration {

	@Inject
	KafkaCluster kafka;

	Properties props;

	@Override
	public Properties getProps() {
		return props;
	}

	@PostConstruct
	public void init() {
		this.props = new Properties();
		props.put(ProducerConfig.RETRIES_CONFIG, 10);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);

	}
}
