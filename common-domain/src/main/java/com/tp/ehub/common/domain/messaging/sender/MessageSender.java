package com.tp.ehub.common.domain.messaging.sender;

import java.io.Closeable;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;

import reactor.core.publisher.Flux;

/**
 * The <code>MessageReceiver</code> sends a reactive stream with messages of the
 * given type to the message broker.
 *
 */
public interface MessageSender extends Closeable {

	/**
	 * The sender function
	 * 
	 * @param <K>
	 *            the type of message key
	 * @param <M>
	 *            the type of message value	 
	 * @param outboundFlux
	 *            the stream of messages to send
	 * @param messageType
	 *            the type of messages to send            
	 */
	<K, M extends Message<K>> void send(Flux<MessageRecord<K, M>> outboundFlux, Class<M> messageType);
}
