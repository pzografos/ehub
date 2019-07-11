package com.tp.ehub.common.infra.messaging.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.MessageStore;
import com.tp.ehub.common.domain.messaging.receiver.KeyMessageReceiver;
import com.tp.ehub.common.domain.messaging.sender.MessageSender;

import reactor.core.publisher.Flux;

public abstract class AbstractMessageStore<K, M extends Message> implements MessageStore<K, M> {

	@Inject
	Logger log;

	KeyMessageReceiver<K, M> receiver;

	MessageSender<K, M> sender;

	protected AbstractMessageStore(MessageSender<K, M> sender, KeyMessageReceiver<K, M> receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}

	@Override
	public Stream<M> getbyKey(K key) {

		List<M> messages = new ArrayList<>();

		receiver.receive(key)
				.doOnError(e -> log.error("Receive failed", e))
				.takeUntil(receiver::isLast)
				.map(MessageRecord::getMessage)
				.doOnNext(m -> messages.add(m))
				.doOnComplete(() -> log.info("Drain for key {} completed", key));

		return messages.stream();
	}

	@Override
	public void publish(Stream<MessageRecord<K, M>> messages) {
		sender.send(Flux.fromStream(messages));
	}
}
