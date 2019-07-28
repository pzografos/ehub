package com.tp.ehub.common.domain.messaging.receiver;

import com.tp.ehub.common.domain.messaging.Message;

import reactor.core.publisher.Flux;

/**
 * The <code>KeyMessageReceiver</code> creates a reactive stream with messages
 * of the given type from the message broker.
 *
 */
public interface MessageReceiver {

	/**
	 * Reads messages from a broker for the given type. All keys are included.
	 * 
	 * @param <K>
	 *            the exact type of message key
	 * @param <M>
	 *            the exact type of message value
	 * @param options
	 *            the receiver options to use
	 * @return the messages for the group
	 */
	<K, M extends Message<K>> Flux<M> receiveAll(Class<M> type);
	
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
	<K, M extends Message<K>> Flux<M> receiveByKey(K key, Class<M> type);	

}
