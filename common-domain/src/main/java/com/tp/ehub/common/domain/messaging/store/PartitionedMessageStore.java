package com.tp.ehub.common.domain.messaging.store;

import java.util.stream.Stream;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * The interface <code>PartitionedMessageStore</code> with methods that supply
 * message partition information.
 *
 */
public interface PartitionedMessageStore extends MessageStore {

	/**
	 * Retrieves all messages for the given key. The user also provides a
	 * partition key to facilitate retrieval.
	 * 
	 * @param <K>
	 *            The type of key for the stored messages
	 * @param <M>
	 *            The type of stored messages
	 * @param key
	 *            the message key
	 * @param partitionKey
	 *            the partition key
	 * @param messageClass
	 *            the implementing message class
	 * @return the list of messages
	 */
	<K, M extends Message<K>> Stream<M> getbyKey(K key, String partitionKey, Class<M> messageClass);
}
