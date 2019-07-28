package com.tp.ehub.common.domain.messaging.receiver;

import com.tp.ehub.common.domain.messaging.Message;

import reactor.core.publisher.Flux;

public interface PartitionedMessageReceiver extends MessageReceiver {

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
	<K, M extends Message<K>> Flux<M> receiveByKey(K key, Class<M> type, String partitionKey);
}
