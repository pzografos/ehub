package com.tp.ehub.common.infra.messaging.kafka.receiver;

import static java.util.Collections.singleton;
import static java.util.Optional.empty;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.KeyValueMessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.receiver.ReactiveMessageReceiver;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Partitioner;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

public class KafkaReactiveReceiver implements ReactiveMessageReceiver {
	
	@Inject
	KafkaCluster kafka;
	
	@Inject
	MessageContainerRegistry registry;
	
	@Inject
	Partitioner partitioner;
	
	private String consumerId;
	
	private Duration pollingInterval;
			
	@PostConstruct
	public void init() {
		configureDefault();	
	}
	
	@Override
	public <K, M extends Message<K>> Flux<M> receive(Class<M> type) {
		
		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);
		
		Optional<Integer> partitionSelected = empty();
		
		Predicate<ConsumerRecord<String, byte[]>> recordFilter = $ -> true;
		
		return KafkaReceiver.create(getReceiverOptions(topic.getName(), partitionSelected)).receiveAtmostOnce()
				.filter(recordFilter)
				.map(new RecordTransformer<K, M>(topic));
	}
	
	public void reset() {
		configureDefault();		
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public void setPollingInterval(Duration pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	private void configureDefault() {
		this.consumerId = UUID.randomUUID().toString();
		this.pollingInterval = Duration.ofMillis(500);
	}
	
	private ReceiverOptions<String, byte[]> getReceiverOptions(String topic, Optional<Integer> partitionSelected) {
		ReceiverOptions<String, byte[]> options =  ReceiverOptions.<String, byte[]> create(consumerProps()).subscription(singleton(topic));
		if (partitionSelected.isPresent()) options = options.assignment(singleton(new TopicPartition(topic, partitionSelected.get())));
		return options;
	}
	
	private Map<String, Object> consumerProps() {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int) pollingInterval.toMillis()),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.GROUP_ID_CONFIG, consumerId),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class));
	}

}
