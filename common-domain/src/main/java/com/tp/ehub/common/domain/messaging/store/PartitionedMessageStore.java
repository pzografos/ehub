package com.tp.ehub.common.domain.messaging.store;

import java.util.stream.Stream;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * The interface <code>PartitionedMessageStore</code> with methods that supply
 * message partition information.
 *
 * @param <K>
 *            The type of key for the messages to store
 * @param <M>
 *            The type of messages to store
 */
public interface PartitionedMessageStore<K, M extends Message<K>> extends MessageStore<K, M> {

	/**
	 * Retrieves all messages for the given key. The user also provides a
	 * partition key to facilitate retireival.
	 * 
	 * @param key
	 *            the message key
	 * @return the list of messages
	 */
	Stream<M> getbyKey(K key, String partitionKey);
}
