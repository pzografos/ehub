package com.tp.ehub.common.infra.messaging.kafka.receiver;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.KeyMessageReceiver;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.KafkaRecord;
import com.tp.ehub.common.infra.messaging.kafka.Topic;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;

public class SinglePartitionKafkaReceiver<K, M extends Message> implements KeyMessageReceiver<K, M> {

	private KafkaCluster kafka;

	private Topic<K, M> topic;

	public SinglePartitionKafkaReceiver(KafkaCluster kafka, Topic<K, M> topic) {
		this.kafka = kafka;
		this.topic = topic;
	}

	@Override
	public Flux<MessageRecord<K, M>> receive(K key) {

		Integer partition = topic.getPartitioner().apply(key);

		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.<String, byte[]> create(consumerProps());

		receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition(topic.getName(), partition)));
		RecordTransformer<K, M> transformer = new RecordTransformer<K, M>(topic);

		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		return receiver.receiveAtmostOnce().map(transformer);
	}

	@Override
	public boolean isLast(MessageRecord<K, M> record) {

		KafkaRecord<K, M> kafkaRecord = (KafkaRecord<K, M>) record;

		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.<String, byte[]> create(consumerProps());
		receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition(topic.getName(), kafkaRecord.getPartition())));

		Consumer<String, byte[]> consumer = ConsumerFactory.INSTANCE.createConsumer(receiverOptions);

		return kafkaRecord.getOffset().equals(getLastOffset(consumer));
	}

	protected Map<String, Object> consumerProps() {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class));
	}

	Long getLastOffset(Consumer<String, byte[]> consumer) {
		Set<TopicPartition> topicPartitions = consumer.assignment();

		return consumer.endOffsets(topicPartitions).entrySet().stream().map(entry -> entry.getValue()).findFirst().get();
	}

}
