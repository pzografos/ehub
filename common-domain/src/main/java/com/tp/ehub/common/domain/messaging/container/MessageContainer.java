package com.tp.ehub.common.domain.messaging.container;

import java.util.function.Function;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * Defines a container for messages. Examples for containers is a Kafka topic or
 * a database table. It is assumed here that messages are serialized and then
 * stored as Strings in the container. So if a container is was an RDBMS table
 * this interface would require modification.
 *
 * @param <K> the type of keys to use for the stored messages
 * @param <M> the type of messages to store
 */
public interface MessageContainer<K, M extends Message<K>> {

	/**
	 * A name that shoud be unique per container
	 * 
	 * @return the container name
	 */
	String getName();
	
	/**
	 * @return the exact class of the messages to store
	 */
	Class<M> getMessageClass();
	
	/**
	 * A function from the message to the <code>String</code> value
	 * responsible for partition calculation.
	 * 
	 * @return the function of <code>M</code> to <code>String</code>
	 * @see #partitionFn()
	 */
	Function<M, String> getPartitionSelector();
	
	/**
	 * The number of partitions this container.
	 * 
	 * @return number of partitions
	 */
	int getPartitions();
}
