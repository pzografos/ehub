package com.tp.ehub.common.infra.messaging.kafka.receiver;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.container.MessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.common.infra.messaging.kafka.KafkaRecord;
import com.tp.ehub.common.infra.messaging.kafka.Partitioner;
import com.tp.ehub.common.infra.producer.ConsumerConfiguration;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

@ApplicationScoped
public class TopicKafkaReceiver implements MessageReceiver {

	@Inject
	MessageContainerRegistry registry;

	@Inject
	Partitioner partitioner;

	@Inject
	@ConsumerConfiguration
	Properties consumerProps;

	@Override
	public <K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveAll(Class<M> type, MessageReceiverOptions options) {

		MessageContainer<K, M> topic = registry.get(type);

		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.<String, byte[]>create(consumerProps).subscription(Collections.singleton(topic.getName()));
		RecordTransformer<K, M> transformer = new RecordTransformer<>(topic);
		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		return receiver.receiveAtmostOnce().map(transformer);
	}

	@Override
	public <K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveByKey(K key, Class<M> type, MessageReceiverOptions options) {

		MessageContainer<K, M> topic = registry.get(type);

		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.create(consumerProps);

		if (Objects.nonNull(options.getPartitionSelector())) {
			Integer partition = partitioner.getPartition(options.getPartitionSelector());
			receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition(topic.getName(), partition)));
		}

		RecordTransformer<K, M> transformer = new RecordTransformer(topic);

		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		return receiver.receiveAtmostOnce().map(transformer);
	}

	@Override
	public <K, M extends Message<K>> boolean isLast(MessageRecord<K, M> record) {

		KafkaRecord<K, M> kafkaRecord = (KafkaRecord<K, M>) record;

		@SuppressWarnings("unchecked") MessageContainer<K, M> container = registry.get(record.getMessage().getClass());

		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.create(consumerProps);
		receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition(container.getName(), kafkaRecord.getPartition())));

		Consumer<String, byte[]> consumer = ConsumerFactory.INSTANCE.createConsumer(receiverOptions);

		return kafkaRecord.getOffset().equals(getLastOffset(consumer));
	}

	Long getLastOffset(Consumer<String, byte[]> consumer) {

		Set<TopicPartition> topicPartitions = consumer.assignment();

		return consumer.endOffsets(topicPartitions).entrySet().stream().map(entry -> entry.getValue()).findFirst().get();
	}

}
