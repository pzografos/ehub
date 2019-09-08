package com.tp.ehub.common.infra.messaging.kafka.receiver;

import static java.util.Collections.singleton;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.KeyValueMessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.receiver.BlockingMessageReceiver;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Partitioner;

import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;

public class KafkaBlockingReceiver implements BlockingMessageReceiver{

	@Inject
	KafkaCluster kafka;
	
	@Inject
	MessageContainerRegistry registry;
	
	@Inject
	Partitioner partitioner;
	
	@Inject
	Logger log;
	
	private String consumerId;
	
	private Duration pollingInterval;
	
	@PostConstruct
	public void init() {
		configureDefault();	
	}

	@Override
	public <K, M extends Message<K>> Stream<M> getByKey(K key, Class<M> type) {

		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);
		
		Optional<Integer> partitionSelected = empty();

		String keyAsString = topic.getKeySerializer().apply(key);
		Predicate<ConsumerRecord<String, byte[]>> recordFilter = record -> nonNull(record.key()) && record.key().equals(keyAsString);

		return receiveAndExitOnDrain(topic, partitionSelected, recordFilter);
	}

	@Override
	public <K, M extends Message<K>> Stream<M> getByKeyAndPartition(K key, Class<M> type, String partitionKey) {

		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(type);

		Integer partition = partitioner.getPartition(partitionKey, topic);
		Optional<Integer> partitionSelected = of(partition);
				
		String keyAsString = topic.getKeySerializer().apply(key);
		Predicate<ConsumerRecord<String, byte[]>> recordFilter = record -> nonNull(record.key()) && record.key().equals(keyAsString);

		return receiveAndExitOnDrain(topic, partitionSelected, recordFilter);
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
	
	private <K, M extends Message<K>> Stream<M> receiveAndExitOnDrain(KeyValueMessageContainer<K, M> topic, Optional<Integer> partitionSelected, Predicate<ConsumerRecord<String, byte[]>> recordFilter){
			
		ReceiverOptions<String, byte[]> options = ReceiverOptions.<String, byte[]> create(consumerProps()); 
						
		Consumer<String, byte[]> consumer =  ConsumerFactory.INSTANCE.createConsumer(options);
		
		if (partitionSelected.isPresent()) {
			consumer.assign(singleton(new TopicPartition(topic.getName(), partitionSelected.get())));
		} else {
			consumer.subscribe(singleton(topic.getName()));
		}
						
		Map<Integer, Long> lastOffsets = readPartitionOffsets(consumer); 
		
		List<M> messages = new ArrayList<>();
						
		try {
			
			consumer.seekToBeginning(consumer.assignment());

			while(!lastOffsets.isEmpty()) {
				
				ConsumerRecords<String, byte[]> records = consumer.poll(pollingInterval);
				
				for (ConsumerRecord<String, byte[]> record : records) {
					
					if (recordFilter.test(record)) {
						messages.add(new RecordTransformer<K, M>(topic).apply(record));
					}
					
					if (lastOffsets.containsKey(record.partition()) && record.offset() >= lastOffsets.get(record.partition())) {
						lastOffsets.remove(record.partition());
					}
		        }
			
			}
		
		} finally {
		    consumer.close();
		}
		
		return messages.stream();
	}
	
	private Map<Integer, Long>  readPartitionOffsets(Consumer<String, byte[]> consumer ){
	
		Collection<TopicPartition> partitions = consumer.assignment();
						
		return consumer.endOffsets(partitions).entrySet().stream()
				.filter(entry -> entry.getValue() != 0L)
				.collect(toMap(entry -> entry.getKey().partition(), entry -> entry.getValue()-1));
	}
	
	private Map<String, Object> consumerProps() {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int) pollingInterval.toMillis()),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.GROUP_ID_CONFIG, consumerId),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class));
	}

}
