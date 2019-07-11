package com.tp.ehub.common.domain.messaging;

import java.util.stream.Stream;

/**
 * The interface <code>MessageStore</code> specifies how to implement a class
 * that can function as Message Store.
 *
 * @param <K>
 *            The type of key for the messages to store
 * @param <M>
 *            The type of messages to store
 */
public interface MessageStore<K, M extends Message> {

	/**
	 * Retrieves all messages for the given key.
	 * 
	 * @param key
	 *            the message key
	 * @return the list of messages
	 */
	Stream<M> getbyKey(K key);

	/**
	 * Publishes a stream of messages to the store
	 * 
	 * @param messages
	 *            the messages to publish
	 */
	void publish(Stream<MessageRecord<K, M>> messages);
}
