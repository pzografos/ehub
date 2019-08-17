package com.tp.ehub.common.infra.messaging.kafka.sender;

import static java.lang.String.format;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.KeyValueMessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.sender.BlockingMessageSender;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Partitioner;

import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.internals.ProducerFactory;

@ApplicationScoped
public class KafkaBlockingSender implements BlockingMessageSender {

	@Inject
	Logger log;

	@Inject
	KafkaCluster kafka;

	@Inject
	Partitioner partitioner;

	@Inject
	MessageContainerRegistry registry;

	private Producer<String, byte[]> producer;

	@PostConstruct()
	public void init() {
		SenderOptions<String, byte[]> senderOptions = SenderOptions.<String, byte[]> create(producerProps());
		senderOptions.stopOnError(true);
		this.producer = ProducerFactory.INSTANCE.createProducer(senderOptions);
	}

	@Override
	public <K, M extends Message<K>> void send(Stream<M> messages, Class<M> messageType) {

		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(messageType);

		messages.forEach(m -> {
			ProducerRecord<String, byte[]> record = producerRecord(topic, m);
			try {
				RecordMetadata metadata = producer.send(record).get();
				log.info(format("Successfully sent message with key %s to topic %s at partion %d with offset %d", m.getKey().toString(), topic.getName(), metadata.partition(), metadata.offset()));
			} catch (Exception e) {
				log.error("Send failed", e);
			}
		});
	}

	@Override
	public void close() throws IOException {
		producer.close();
	}

	protected Map<String, Object> producerProps() {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ProducerConfig.RETRIES_CONFIG, 10), new AbstractMap.SimpleEntry<>(ProducerConfig.ACKS_CONFIG, "all"),
				new AbstractMap.SimpleEntry<>(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()),
				new AbstractMap.SimpleEntry<>(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class),
				new AbstractMap.SimpleEntry<>(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class));
	}

	private <K, M extends Message<K>> ProducerRecord<String, byte[]> producerRecord(KeyValueMessageContainer<K, M> topic, M message) {
		String recordKey = topic.getKeySerializer().apply(message.getKey());
		byte[] recordValue = topic.getValueSerializer().apply(message);
		Integer partition = partitioner.getPartition(topic.getPartitionSelector().apply(message), topic);
		return new ProducerRecord<String, byte[]>(topic.getName(), partition, recordKey, recordValue);
	}

}
