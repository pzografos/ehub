package com.tp.ehub.common.infra.sender.factory.kafka.sender;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.container.MessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;
import com.tp.ehub.common.domain.messaging.sender.MessageSender;
import com.tp.ehub.common.domain.messaging.sender.MessageSenderConfiguration;
import com.tp.ehub.common.infra.sender.factory.kafka.Partitioner;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

public class TopicKafkaSender implements MessageSender {

	@Inject
	Logger log;

	@Inject
	Partitioner partitioner;

	@Inject
	MessageContainerRegistry registry;

	@Inject
	MessageSenderConfiguration messageSenderConfiguration;

	private KafkaSender<String, byte[]> sender;

	@PostConstruct()
	public void init() {
		SenderOptions<String, byte[]> senderOptions = SenderOptions.create(messageSenderConfiguration.getProps());
		senderOptions.stopOnError(true);
		this.sender = KafkaSender.create(senderOptions);
	}

	@Override
	public <K, M extends Message<K>> void send(Flux<MessageRecord<K, M>> outboundFlux, Class<M> messageType) {

		MessageContainer<K, M> topic = registry.get(messageType);

		sender.send(outboundFlux.map(r -> senderRecord(topic, r)))
				.doOnError(e -> log.error("Send failed", e))
				.doOnNext(r -> log.info(String.format("Successfully sent message to topic %s at partion %d with offset %d", topic.getName(), r.recordMetadata().partition(),
						r.recordMetadata().offset())))
				.subscribe();
	}

	@Override
	public void close() throws IOException {
		sender.close();
	}

	private <K, M extends Message<K>> SenderRecord<String, byte[], String> senderRecord(MessageContainer<K, M> topic, MessageRecord<K, M> messageRecord) {
		String recordKey = topic.getKeySerializer().apply(messageRecord.getKey());
		byte[] recordValue = topic.getValueSerializer().apply(messageRecord.getMessage());
		Integer partition = partitioner.getPartition(topic.getPartitionSelector().apply(messageRecord.getMessage()));
		ProducerRecord<String, byte[]> pr = new ProducerRecord<String, byte[]>(topic.getName(), partition, recordKey, recordValue);
		return SenderRecord.create(pr, recordKey);
	}
}
