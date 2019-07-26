package com.tp.ehub.common.domain.messaging.container;

import java.util.function.Function;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * Defines a message container that would store messages in form of key-value
 * pairs. The messages are serialized and then stored as <code>String</code> for
 * the key and <code>byte[]</code> in the container. This works well with Kafka
 * topics or NOSQL containers. It would NOT work with a RDBMS table. 
 * 
 */
public interface KeyValueMessageContainer<K, M extends Message<K>> extends MessageContainer<K,M> {

	/**
	 * @return a function to de-serialize message keys
	 */
	Function<String, K> getKeyDeserializer();

	/**
	 * @return a function to serialize message keys with
	 */
	Function<K, String> getKeySerializer();

	/**
	 * @return a function to de-serialize messages
	 */
	Function<byte[], M> getValueDeserializer();

	/**
	 * @return a function to serialize messages with
	 */
	Function<M, byte[]> getValueSerializer();
}
