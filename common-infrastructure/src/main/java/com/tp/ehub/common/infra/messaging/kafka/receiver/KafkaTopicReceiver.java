package com.tp.ehub.common.infra.messaging.kafka.receiver;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.container.KeyValueMessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.receiver.PartitionedMessageReceiver;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.KafkaRecord;
import com.tp.ehub.common.infra.messaging.kafka.Partitioner;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;

public class KafkaTopicReceiver implements PartitionedMessageReceiver {
	
	@Inject
	KafkaCluster kafka;
	
	@Inject
	MessageContainerRegistry registry;
	
	@Inject
	Partitioner partitioner;
	
	private String consumerId = UUID.randomUUID().toString();
	
	private Long pollingInterval = 500L;
		
	@PostConstruct
	public void init() {
		configureDefault();	
	}
	
	@Override
	public <K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveAll(Class<M> type) {
		
		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);
		ReceiverOptions<String, byte[]> receiverOptions = receiverOptions().subscription(Collections.singleton(topic.getName()));
		RecordTransformer<K, M> transformer = new RecordTransformer<K, M>(topic);
		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		
		return receiver.receiveAtmostOnce().map(transformer);
	}

	@Override
	public <K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveByKey(K key, Class<M> type) {

		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);

		ReceiverOptions<String, byte[]> receiverOptions = receiverOptions();

		RecordTransformer<K, M> transformer = new RecordTransformer<K, M>(topic);

		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		return receiver.receiveAtmostOnce().map(transformer);
	}

	@Override
	public <K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveByKey(K key, Class<M> type, String partitionKey) {
		
		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);

		ReceiverOptions<String, byte[]> receiverOptions = receiverOptions();

		Integer partition = partitioner.getPartition(partitionKey, topic);
		receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition(topic.getName(), partition)));

		RecordTransformer<K, M> transformer = new RecordTransformer<K, M>(topic);

		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		return receiver.receiveAtmostOnce().map(transformer);
	}

	@Override
	public <K, M extends Message<K>> boolean isLast(MessageRecord<K, M> record) {
		
		KafkaRecord<K, M> kafkaRecord = (KafkaRecord<K, M>) record;
		
		@SuppressWarnings("unchecked")
		MessageContainer<K, M> container = registry.get(record.getMessage().getClass());
		
		ReceiverOptions<String, byte[]> receiverOptions = receiverOptions();
		receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition(container.getName(), kafkaRecord.getPartition())));

		Consumer<String, byte[]> consumer = ConsumerFactory.INSTANCE.createConsumer(receiverOptions);

		return kafkaRecord.getOffset().equals(getLastOffset(consumer));
	}
	
	public Long getLastOffset(Consumer<String, byte[]> consumer) {
		Set<TopicPartition> topicPartitions = consumer.assignment();

		return consumer.endOffsets(topicPartitions).entrySet().stream().map(entry -> entry.getValue()).findFirst().get();
	}
	
	public void reset() {
		configureDefault();		
	}

	private void configureDefault() {
		this.consumerId = UUID.randomUUID().toString();
		this.pollingInterval = 500L;
	}
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public void setPollingInterval(Long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	private ReceiverOptions<String, byte[]> receiverOptions() {
		return ReceiverOptions.<String, byte[]> create(consumerProps());
	}
	
	private Map<String, Object> consumerProps() {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, pollingInterval),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.GROUP_ID_CONFIG, consumerId),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class));
	}

}
