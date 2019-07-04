package com.tp.ehub.repository.message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.ehub.model.messaging.Message;
import com.tp.ehub.model.messaging.MessageRecord;
import com.tp.ehub.repository.MessageStore;
import com.tp.ehub.service.messaging.KeyMessageReceiver;
import com.tp.ehub.service.messaging.MessageSender;

import reactor.core.publisher.Flux;

public abstract class AbstractMessageStore<K, M extends Message> implements MessageStore<K, M> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageStore.class);

	@Inject
	private KeyMessageReceiver<K, M> receiver;

	@Inject
	private MessageSender<K, M> sender;

	@Override
	public Stream<M> getbyKey(K key) {
		
		List<M> messages = new ArrayList<>();
		
		receiver.receive(key)
				.doOnError(e -> LOGGER.error("Receive failed", e))
				.takeUntil(receiver::isLast)
				.map(MessageRecord::getMessage)
				.doOnNext(m -> messages.add(m))
				.doOnComplete(() -> LOGGER.info("Drain for key {} completed", key));
		
		return messages.stream();
	}

	@Override
	public void publish(Stream<MessageRecord<K, M>> messages) {
		sender.send(Flux.fromStream(messages));
	}
}
