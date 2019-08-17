package com.tp.ehub.common.domain.messaging.sender;

import java.io.Closeable;

import com.tp.ehub.common.domain.messaging.Message;

import reactor.core.publisher.Flux;

/**
 * The <code>ReactiveMessageSender</code> provides methods to send a reactive
 * stream with messages to the message broker.
 *
 */
public interface ReactiveMessageSender extends Closeable {

	/**
	 * Sends messages of the given type to the message broker
	 * 
	 * @param <K>
	 *            the type of message key
	 * @param <M>
	 *            the type of message value	 
	 * @param outboundFlux
	 *            the reactive stream of messages to send
	 * @param messageType
	 *            the type of messages to send            
	 */
	<K, M extends Message<K>> void send(Flux<M> outboundFlux, Class<M> messageType);
}
