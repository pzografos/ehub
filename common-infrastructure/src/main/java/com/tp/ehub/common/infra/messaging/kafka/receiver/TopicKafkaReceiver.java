package com.tp.ehub.common.infra.messaging.kafka.receiver;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.container.MessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.KafkaRecord;
import com.tp.ehub.common.infra.messaging.kafka.Partitioner;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;

@ApplicationScoped
public class TopicKafkaReceiver implements MessageReceiver {

	@Inject
	KafkaCluster kafka;
	
	@Inject
	MessageContainerRegistry registry;
	
	@Inject
	Partitioner partitioner;

	@Override
	public <K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveAll(Class<M> type, MessageReceiverOptions options) {
		
		MessageContainer<K, M> topic = registry.get(type);
		
		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.<String, byte[]> create(consumerProps(options))
				.subscription(Collections.singleton(topic.getName()));
		RecordTransformer<K, M> transformer = new RecordTransformer<K, M>(topic);
		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		return receiver.receiveAtmostOnce().map(transformer);
	}

	@Override
	public <K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveByKey(K key, Class<M> type, MessageReceiverOptions options) {
		
		MessageContainer<K, M> topic = registry.get(type);
		
		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.<String, byte[]> create(consumerProps(options));

		if (Objects.nonNull(options.getPartitionSelector())) {
			Integer partition = partitioner.getPartition(options.getPartitionSelector());
			receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition(topic.getName(), partition)));
		}
		
		RecordTransformer<K, M> transformer = new RecordTransformer<K, M>(topic);

		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		return receiver.receiveAtmostOnce().map(transformer);
	}

	@Override
	public <K, M extends Message<K>> boolean isLast(MessageRecord<K, M> record) {
		
		KafkaRecord<K, M> kafkaRecord = (KafkaRecord<K, M>) record;
		
		@SuppressWarnings("unchecked")
		MessageContainer<K, M> container = registry.get(record.getMessage().getClass());
		
		MessageReceiverOptions options = new MessageReceiverOptions();
		options.setConsumerId(UUID.randomUUID().toString());
		
		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.<String, byte[]> create(consumerProps(options));
		receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition(container.getName(), kafkaRecord.getPartition())));

		Consumer<String, byte[]> consumer = ConsumerFactory.INSTANCE.createConsumer(receiverOptions);

		return kafkaRecord.getOffset().equals(getLastOffset(consumer));
	}
	
	Long getLastOffset(Consumer<String, byte[]> consumer) {
		Set<TopicPartition> topicPartitions = consumer.assignment();

		return consumer.endOffsets(topicPartitions).entrySet().stream().map(entry -> entry.getValue()).findFirst().get();
	}
	
	protected Map<String, Object> consumerProps(MessageReceiverOptions options) {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, String.valueOf(500L)),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()), new AbstractMap.SimpleEntry<>(ConsumerConfig.GROUP_ID_CONFIG, options.getConsumerId()),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class));
	}

}
