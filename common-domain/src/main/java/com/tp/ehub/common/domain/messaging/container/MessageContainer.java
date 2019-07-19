package com.tp.ehub.common.domain.messaging.container;

import java.util.function.Function;

import com.tp.ehub.common.domain.messaging.Message;

public interface MessageContainer<K, M extends Message<K>> {

	String getName();
	
	Class<M> getMessageClass();

	Function<String, K> getKeyDeserializer();

	Function<K, String> getKeySerializer();

	Function<byte[], M> getValueDeserializer();

	Function<M, byte[]> getValueSerializer();
	
	/**
	 * A function from the message to the <code>String</code> value
	 * responsible for partition calculation.
	 * 
	 * @return the function of <code>M</code> to <code>String</code>
	 * @see #partitionFn()
	 */
	Function<M, String> getPartitionSelector();
}
