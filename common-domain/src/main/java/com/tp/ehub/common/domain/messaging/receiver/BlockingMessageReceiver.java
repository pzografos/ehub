package com.tp.ehub.common.domain.messaging.receiver;

import java.util.stream.Stream;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * The <code>BlockingMessageReceiver</code> provides methods to get a stream of
 * messages of the given type from the message broker.
 *
 */
public interface BlockingMessageReceiver {

	/**
	 * Gets a reactive stream with messages of the given type from the message
	 * broker for a specific key.
	 * 
     * @param <K>
     *            the exact type of message key
     * @param <M>
     *            the exact type of message value        	 
	 * @param options
	 *            the receiver options to use	 
	 * @return the reactive stream
	 */
	<K, M extends Message<K>> Stream<M> getByKey(K key, Class<M> type);
	
	/**
	 * Gets a reactive stream with messages of the given type from the message
	 * broker for a specific key.
	 * 
	 * @param <K>
	 *            the exact type of message key
	 * @param <M>
	 *            the exact type of message value
	 * @param key
	 *            the message key
	 * @param type
	 *            the implementing message class
	 * @param partitionKey
	 *            the partition key
	 * @return the reactive stream
	 */
	<K, M extends Message<K>> Stream<M> getByKeyAndPartition(K key, Class<M> type, String partitionKey);
}
