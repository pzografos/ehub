package com.tp.ehub.common.infra.messaging.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.sender.MessageSender;
import com.tp.ehub.common.domain.messaging.store.PartitionedMessageStore;
import com.tp.ehub.common.infra.messaging.kafka.receiver.KafkaTopicReceiver;

import reactor.core.publisher.Flux;

@ApplicationScoped
public class KafkaMessageStore implements PartitionedMessageStore {

	@Inject
	Logger log;

	@Inject
	KafkaTopicReceiver receiver;

	@Inject
	MessageSender sender;
	
	@Override
	public <K, M extends Message<K>> Stream<M> getbyKey(K key, Class<M> messageClass) {

		List<M> messages = new ArrayList<>();

		receiver.setAttach(false);
		receiver.receiveByKey(key, messageClass)
				.doOnError(e -> log.error("Receive failed", e))
				.doOnNext(m -> messages.add(m))
				.doOnComplete(() -> log.info("Drain for key {} completed", key));
		receiver.reset();

		return messages.stream();
	}
	
	@Override
	public <K, M extends Message<K>> Stream<M> getbyKey(K key, String partitionKey, Class<M> messageClass) {
		
		List<M> messages = new ArrayList<>();

		receiver.setAttach(false);
		receiver.receiveByKey(key, messageClass, partitionKey)
				.doOnError(e -> log.error("Receive failed", e))
				.doOnNext(m -> messages.add(m))
				.doOnComplete(() -> log.info("Drain for key {} completed", key));
		receiver.reset();

		return messages.stream();
	}

	@Override
	public <K, M extends Message<K>> void publish(Stream<M> messages, Class<M> messageClass) {
		sender.send(Flux.fromStream(messages), messageClass);
	}

}
