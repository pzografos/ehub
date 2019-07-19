package com.tp.ehub.common.infra.messaging.kafka.container;

import static org.apache.kafka.common.utils.Utils.murmur2;
import static org.apache.kafka.common.utils.Utils.toPositive;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.MessageContainer;

/**
 * Provides all the implementation details of a Kafka topic
 *
 */
public interface Topic<K, M extends Message<K>> extends MessageContainer<K, M> {

	Function<String, K> getKeyDeserializer();

	Function<K, String> getKeySerializer();

	Function<byte[], M> getValueDeserializer();

	Function<M, byte[]> getValueSerializer();
	
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
	 * Gives the owning partition for the provided message
	 * 
	 * @return the partition for the message
	 */
	default Integer getPartition(M message) {
		String partitionKey = getPartitionSelector().apply(message);
		return getPartition(partitionKey);
	}

	/**
	 * Gives the owning partition for the provided partition key
	 * 
	 * @see #partitions()
	 * @return the partition for the key
	 */
	default Integer getPartition(String partitionKey) {
		return toPositive(murmur2(partitionKey.getBytes(StandardCharsets.UTF_8))) % getPartitions();
	}

	/**
	 * The number of partitions this topic has. Defaults to 15.
	 * 
	 * @return number of partitions
	 */
	default int getPartitions() {
		return 15;
	}

}
