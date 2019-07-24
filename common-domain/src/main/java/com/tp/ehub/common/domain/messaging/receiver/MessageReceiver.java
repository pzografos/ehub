package com.tp.ehub.common.domain.messaging.receiver;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;

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
	 * @param type
	 *            the class of the message to read
	 * @param options
	 *            the receiver options to use
	 * @return the messages for the group
	 */
	<K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveAll(Class<M> type, MessageReceiverOptions options);
	
	/**
	 * Gets a reactive stream with messages of the given type from the message
	 * broker for a specific key.
	 * 
     * @param <K>
     *            the exact type of message key
     * @param <M>
     *            the exact type of message value 
	 * @param type
	 *            the class of the message to read       	 
	 * @param options
	 *            the receiver options to use	 
	 * @return the reactive stream
	 */
	<K, M extends Message<K>> Flux<MessageRecord<K, M>> receiveByKey(K key, Class<M> type, MessageReceiverOptions options);

	/**
	 * Verifies if a given record is last known record for the key at the moment
	 * of querying.
	 * 
     * @param <K>
     *            the exact type of message key
     * @param <M>
     *            the exact type of message value 
	 * @param record
	 *            the given record
	 * @return true if the given record is last known record for the key.
	 */
	<K, M extends Message<K>> boolean isLast(MessageRecord<K, M> record);	

}
