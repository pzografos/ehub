package com.tp.ehub.common.infra.messaging.kafka.receiver;

import static java.util.Collections.singleton;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.KeyValueMessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.receiver.PartitionedMessageReceiver;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
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
	
	private String consumerId;
	
	private Long pollingInterval;
	
	private boolean attach; 
		
	@PostConstruct
	public void init() {
		configureDefault();	
	}
	
	@Override
	public <K, M extends Message<K>> Flux<M> receiveAll(Class<M> type) {
		
		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);
		
		Optional<Integer> partitionSelected = empty();
		
		Predicate<ConsumerRecord<String, byte[]>> recordFilter = $ -> true;
		
		return attach ? receiveAttaced(topic, partitionSelected, recordFilter) : receiveAndExitOnDrain(topic, partitionSelected, recordFilter);
	}

	@Override
	public <K, M extends Message<K>> Flux<M> receiveByKey(K key, Class<M> type) {

		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);
		
		Optional<Integer> partitionSelected = empty();
		
		String keyAsString = topic.getKeySerializer().apply(key);
		Predicate<ConsumerRecord<String, byte[]>> recordFilter = record -> nonNull(record.key()) && record.key().equals(keyAsString);
		
		return attach ? receiveAttaced(topic, partitionSelected, recordFilter) : receiveAndExitOnDrain(topic, partitionSelected, recordFilter);
	}

	@Override
	public <K, M extends Message<K>> Flux<M> receiveByKey(K key, Class<M> type, String partitionKey) {
		
		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);
		
		Integer partition = partitioner.getPartition(partitionKey, topic);
		Optional<Integer> partitionSelected = of(partition);
		
		String keyAsString = topic.getKeySerializer().apply(key);
		Predicate<ConsumerRecord<String, byte[]>> recordFilter = record -> nonNull(record.key()) && record.key().equals(keyAsString);

		return attach ? receiveAttaced(topic, partitionSelected, recordFilter) : receiveAndExitOnDrain(topic, partitionSelected, recordFilter);
	}
	
	private <K, M extends Message<K>> Flux<M> receiveAttaced(KeyValueMessageContainer<K, M> topic, Optional<Integer> partitionSelected, Predicate<ConsumerRecord<String, byte[]>> recordFilter){
		return KafkaReceiver.create(getReceiverOptions(topic.getName(), partitionSelected)).receiveAtmostOnce()
				.filter(recordFilter)
				.map(new RecordTransformer<K, M>(topic));
	}
	
	private <K, M extends Message<K>> Flux<M> receiveAndExitOnDrain(KeyValueMessageContainer<K, M> topic, Optional<Integer> partitionSelected, Predicate<ConsumerRecord<String, byte[]>> recordFilter){
		
		Map<Integer, Long> partitionOffsets = readPartitionOffsets(topic.getName(), partitionSelected); 
		List<Integer> partitionsWithData = partitionOffsets.keySet().stream().collect(toList());
		
		return KafkaReceiver.create(getReceiverOptions(topic.getName(), partitionSelected)).receiveAtmostOnce()
				.filter(recordFilter)
				.doOnNext(record -> {
					if (record.offset() == partitionOffsets.get(record.partition())) {
						partitionsWithData.remove(record.partition());
					}
				})
				.takeUntil(record -> partitionsWithData.isEmpty())
				.map(new RecordTransformer<K, M>(topic));
	}
	
	public void reset() {
		configureDefault();		
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public void setPollingInterval(Long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	public void setAttach(boolean attach) {
		this.attach = attach;
	}

	private void configureDefault() {
		this.consumerId = UUID.randomUUID().toString();
		this.pollingInterval = 500L;
		this.attach = true;
	}
	
	private Map<Integer, Long>  readPartitionOffsets(String topic, Optional<Integer> partitionSelected){
		
		ReceiverOptions<String, byte[]> receiverOptions = getReceiverOptions(topic, partitionSelected);
		Consumer<String, byte[]> consumer = ConsumerFactory.INSTANCE.createConsumer(receiverOptions);
		
		Collection<TopicPartition> partitions = receiverOptions.assignment();
		
		return consumer.endOffsets(partitions).entrySet().stream()				
			.collect(toMap(
					entry -> entry.getKey().partition(), 
					Map.Entry::getValue));
	}
	
	private ReceiverOptions<String, byte[]> getReceiverOptions(String topic, Optional<Integer> partitionSelected) {
		ReceiverOptions<String, byte[]> options =  ReceiverOptions.<String, byte[]> create(consumerProps()).subscription(singleton(topic));
		if (partitionSelected.isPresent()) options = options.assignment(singleton(new TopicPartition(topic, partitionSelected.get())));
		return options;
	}
	
	private Map<String, Object> consumerProps() {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, pollingInterval),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.GROUP_ID_CONFIG, consumerId),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class));
	}

}
