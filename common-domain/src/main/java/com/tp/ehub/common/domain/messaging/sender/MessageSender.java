package com.tp.ehub.common.domain.messaging.sender;

import java.io.Closeable;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;

import reactor.core.publisher.Flux;

/**
 * The <code>MessageReceiver</code> sends a reactive stream with messages of the
 * given type to the message broker.
 *
 * @param <K>
 *            the type of message key
 * @param <M>
 *            the type of message value
 */
public interface MessageSender<K, M extends Message> extends Closeable {

	/**
	 * The sender function
	 * 
	 * @param outboundFlux
	 *            the stream of messages to send
	 */
	void send(Flux<MessageRecord<K, M>> outboundFlux);
}
