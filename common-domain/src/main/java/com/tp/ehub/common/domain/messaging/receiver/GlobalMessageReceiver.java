package com.tp.ehub.common.domain.messaging.receiver;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;

import reactor.core.publisher.Flux;

/**
 * The <code>KeyMessageReceiver</code> creates a reactive stream with messages
 * of the given type from the message broker that includes all keys.
 *
 * @param <K>
 *            the type of message key
 * @param <M>
 *            the type of message value
 */
public interface GlobalMessageReceiver<K, M extends Message> {

	/**
	 * Reads messages from a broker for the given type. All keys are included.
	 * <p>
	 * The method allows the user to provide a consumer id so that the message
	 * broker provider may store metadata for each consumers and allow consumers
	 * to continue from where they left off. See Kafka consumer groups for an
	 * example. The application relies on the existence of this feature.
	 * </p>
	 * 
	 * @param consumerId
	 *            the consumer ID
	 * @param fromStart
	 *            if this is a new consumer should we provide all messages or
	 *            just the ones that arrive from the function call onwards
	 * @return the messages for the group
	 */
	Flux<MessageRecord<K, M>> receive(String consumerId, boolean fromStart);

}