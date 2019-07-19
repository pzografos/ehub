package com.tp.ehub.common.infra.receiver.bean;

import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverConfiguration;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.common.infra.qualifier.Random;
import com.tp.ehub.common.infra.sender.factory.kafka.KafkaCluster;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Properties;
import java.util.UUID;

@Random
public class RandomMessageReceiverConfiguration implements MessageReceiverConfiguration {

	@Inject
	KafkaCluster kafka;

	MessageReceiverOptions options;

	Properties props;

	@Override
	public Properties getProps() {
		return props;
	}

	@Override
	public MessageReceiverOptions getOptions() {
		return options;
	}

	@PostConstruct
	public void init() {

		this.options = new MessageReceiverOptions(UUID.randomUUID().toString(), true);

		this.props = new Properties();
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, String.valueOf(500L));
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, options.getConsumerId());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

	}

}
