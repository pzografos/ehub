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
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.sender.MessageSender;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.container.Topic;

import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

public class TopicKafkaSender implements MessageSender {

	@Inject
	Logger log;

	@Inject
	private KafkaCluster kafka;
	
	@Inject
	private MessageContainerRegistry registry;
	
	private KafkaSender<String, byte[]> sender;
	
	@PostConstruct()
	public void init() {
		SenderOptions<String, byte[]> senderOptions = SenderOptions.<String, byte[]> create(producerProps());
		senderOptions.stopOnError(true);
		this.sender = KafkaSender.create(senderOptions);
	}

	@Override
	public <K, M extends Message<K>> void send(Flux<MessageRecord<K, M>> outboundFlux, Class<M> messageType) {
		
		Topic<K, M> topic = (Topic<K, M>) registry.get(messageType);
		
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

	private <K, M extends Message<K>> SenderRecord<String, byte[], String> senderRecord(Topic<K, M> topic, MessageRecord<K, M> messageRecord) {
		String recordKey = topic.getKeySerializer().apply(messageRecord.getKey());
		byte[] recordValue = topic.getValueSerializer().apply(messageRecord.getMessage());
		Integer partition = topic.getPartition(messageRecord.getMessage());
		ProducerRecord<String, byte[]> pr = new ProducerRecord<String, byte[]>(topic.getName(), partition, recordKey, recordValue);
		return SenderRecord.create(pr, recordKey);
	}
}
