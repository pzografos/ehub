package com.tp.ehub.common.domain.messaging.store;

import java.util.stream.Stream;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * The interface <code>MessageStore</code> specifies how to implement a class
 * that can function as Message Store.
 */
public interface MessageStore {

	/**
	 * Retrieves all messages for the given key.
	 * 
	 * @param <K>
	 *            The type of key for the stored messages
	 * @param <M>
	 *            The type of stored messages
	 * @param key
	 *            the message key
	 * @param messageClass
	 *            the implementing message class
	 * @return the list of messages
	 */
	<K, M extends Message<K>> Stream<M> getbyKey(K key, Class<M> messageClass);

	/**
	 * Publishes a stream of messages to the store
	 * 
	 * @param <K>
	 *            The type of key for the stored messages
	 * @param <M>
	 *            The type of stored messages
	 * @param messages
	 *            the messages to publish
	 * @param messageClass
	 *            the implementing message class
	 */
	<K, M extends Message<K>> void publish(Stream<M> messages, Class<M> messageClass);
}
