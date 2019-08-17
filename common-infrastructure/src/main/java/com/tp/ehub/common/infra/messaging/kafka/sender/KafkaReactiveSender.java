package com.tp.ehub.common.infra.messaging.kafka.sender;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.KeyValueMessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.sender.ReactiveMessageSender;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Partitioner;

import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

public class KafkaReactiveSender implements ReactiveMessageSender {

	@Inject
	Logger log;

	@Inject
	KafkaCluster kafka;
	
	@Inject
	Partitioner partitioner;

	@Inject
	MessageContainerRegistry registry;
	
	private KafkaSender<String, byte[]> sender;
	
	@PostConstruct()
	public void init() {
		SenderOptions<String, byte[]> senderOptions = SenderOptions.<String, byte[]> create(producerProps());
		senderOptions.stopOnError(true);
		this.sender = KafkaSender.create(senderOptions);
	}

	@Override
	public <K, M extends Message<K>> void send(Flux<M> outboundFlux, Class<M> messageType) {
		
		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(messageType);
		
		sender.send(outboundFlux.map( r -> senderRecord(topic, r)))
			.doOnError(e -> log.error("Send failed", e))
			.doOnNext(r -> log.info(String.format("Successfully sent message to topic %s at partion %d with offset %d", topic.getName(), r.recordMetadata().partition(),
					r.recordMetadata().offset())))
			.subscribe();
	}
	
	@Override
	public void close() throws IOException {
		sender.close();
	}

	protected Map<String, Object> producerProps() {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ProducerConfig.RETRIES_CONFIG, 10),
				new AbstractMap.SimpleEntry<>(ProducerConfig.ACKS_CONFIG, "all"),
				new AbstractMap.SimpleEntry<>(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()),
				new AbstractMap.SimpleEntry<>(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class),
				new AbstractMap.SimpleEntry<>(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class));
	}

	private <K, M extends Message<K>> SenderRecord<String, byte[], String> senderRecord(KeyValueMessageContainer<K, M> topic, M message) {
		String recordKey = topic.getKeySerializer().apply(message.getKey());
		byte[] recordValue = topic.getValueSerializer().apply(message);
		Integer partition = partitioner.getPartition(topic.getPartitionSelector().apply(message), topic);
		ProducerRecord<String, byte[]> pr = new ProducerRecord<String, byte[]>(topic.getName(), partition, recordKey, recordValue);
		return SenderRecord.create(pr, recordKey);
	}
}
