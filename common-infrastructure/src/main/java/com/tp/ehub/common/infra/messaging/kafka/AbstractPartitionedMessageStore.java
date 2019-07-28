package com.tp.ehub.common.infra.messaging.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.sender.MessageSender;
import com.tp.ehub.common.domain.messaging.store.PartitionedMessageStore;
import com.tp.ehub.common.infra.messaging.kafka.receiver.KafkaTopicReceiver;

import reactor.core.publisher.Flux;

public abstract class AbstractPartitionedMessageStore<K, M extends Message<K>> implements PartitionedMessageStore<K, M> {

	@Inject
	Logger log;

	@Inject
	KafkaTopicReceiver receiver;

	@Inject
	MessageSender sender;
	
	Class<M> messageClass;

	protected AbstractPartitionedMessageStore(Class<M> messageClass) {
		this.messageClass = messageClass;
	}

	@Override
	public Stream<M> getbyKey(K key) {

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
	public Stream<M> getbyKey(K key, String partitionKey) {
		
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
	public void publish(Stream<M> messages) {
		sender.send(Flux.fromStream(messages), messageClass);
	}

}
