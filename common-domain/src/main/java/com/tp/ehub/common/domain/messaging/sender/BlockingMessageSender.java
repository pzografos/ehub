package com.tp.ehub.common.domain.messaging.sender;

import java.io.Closeable;
import java.util.stream.Stream;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * The <code>BlockingMessageSender</code> provides methods to send a stream with
 * messages to the message broker in a blocking manner
 *
 */
public interface BlockingMessageSender extends Closeable {

	/**
	 * Sends messages of the given type to the message broker
	 * 
	 * @param <K>
	 *            the type of message key
	 * @param <M>
	 *            the type of message value	 
	 * @param outboundStream
	 *            the stream of messages to send
	 * @param messageType
	 *            the type of messages to send            
	 */
	<K, M extends Message<K>> void send(Stream<M> outboundStream, Class<M> messageType);
}
