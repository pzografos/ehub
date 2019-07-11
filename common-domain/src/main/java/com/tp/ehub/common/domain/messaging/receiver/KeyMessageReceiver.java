package com.tp.ehub.common.domain.messaging.receiver;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;

import reactor.core.publisher.Flux;

/**
 * The <code>KeyMessageReceiver</code> creates a reactive stream with messages
 * of the given key from the message broker.
 *
 * @param <K>
 *            the type of message key
 * @param <M>
 *            the type of message value
 */
public interface KeyMessageReceiver<K, M extends Message> {

	/**
	 * Gets a reactive stream with messages of the given type from the message
	 * broker for a specific key.
	 * 
	 * @param key
	 *            the message key
	 * @return the reactive stream
	 */
	Flux<MessageRecord<K, M>> receive(K key);

	/**
	 * Verifies if a given record is last known record for the key at the moment
	 * of querying.
	 * 
	 * @param record
	 *            the given record
	 * @return true if the given record is last known record for the key.
	 */
	boolean isLast(MessageRecord<K, M> record);
}
