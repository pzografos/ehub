package com.tp.ehub.messaging.kafka.service;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.ehub.messaging.kafka.KafkaCluster;
import com.tp.ehub.messaging.kafka.Topic;
import com.tp.ehub.model.messaging.Message;
import com.tp.ehub.model.messaging.MessageRecord;
import com.tp.ehub.service.messaging.MessageSender;

import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

public class TopicKafkaSender<K, M extends Message> implements MessageSender<K, M> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopicKafkaSender.class);

	private KafkaCluster kafka;

	private Topic<K, M> topic;

	private KafkaSender<String, byte[]> sender;

	public TopicKafkaSender(KafkaCluster kafka, Topic<K, M> topic) {
		this.kafka = kafka;
		this.topic = topic;
		SenderOptions<String, byte[]> senderOptions = SenderOptions.<String, byte[]> create(producerProps());
		senderOptions.stopOnError(true);
		this.sender = KafkaSender.create(senderOptions);
	}

	@Override
	public void send(Flux<MessageRecord<K, M>> outboundFlux) {
		sender.send(outboundFlux.map(this::senderRecord))
				.doOnError(e -> LOGGER.error("Send failed", e))
				.doOnNext(r -> LOGGER.info(String.format("Successfully sent message to topic %s at partion %d with offset %d", topic.getName(), r.recordMetadata().partition(),
						r.recordMetadata().offset())))
				.subscribe();
	}

	@Override
	public void close() throws IOException {
		sender.close();
	}

	protected Map<String, Object> producerProps() {
		return Map.ofEntries(new AbstractMap.SimpleEntry<>(ProducerConfig.RETRIES_CONFIG, topic.getProducerRetries()),
				new AbstractMap.SimpleEntry<>(ProducerConfig.ACKS_CONFIG, topic.getProducerAcknowledgements()),
				new AbstractMap.SimpleEntry<>(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()),
				new AbstractMap.SimpleEntry<>(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class),
				new AbstractMap.SimpleEntry<>(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class));
	}

	private SenderRecord<String, byte[], String> senderRecord(MessageRecord<K, M> messageRecord) {
		String recordKey = topic.getKeySerializer().apply(messageRecord.getKey());
		byte[] recordValue = topic.getValueSerializer().apply(messageRecord.getMessage());
		Integer partition = topic.getPartitioner().apply(messageRecord.getKey());
		ProducerRecord<String, byte[]> pr = new ProducerRecord<String, byte[]>(topic.getName(), partition, recordKey, recordValue);
		return SenderRecord.create(pr, recordKey);
	}

}
