package com.tp.ehub.common.infra.messaging.kafka;

import static org.apache.kafka.common.utils.Utils.murmur2;
import static org.apache.kafka.common.utils.Utils.toPositive;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * Provides all the implementation details of a Kafka topic
 *
 */
public interface Topic<K, M extends Message> {

	String getName();

	Function<String, K> getKeyDeserializer();

	Function<K, String> getKeySerializer();

	Function<byte[], M> getValueDeserializer();

	Function<M, byte[]> getValueSerializer();

	/**
	 * A function from the message key to the <code>String</code> value
	 * responsible for partition calculation.
	 * 
	 * @return the function of <code>K</code> to <code>String</code>
	 * @see #partitionFn()
	 */
	Function<K, String> getPartitionSelector();

	/**
	 * Gives the owning partition of the provided key based on
	 * <code>#partitionFn()<code>
	 * 
	 * @see #partitions()
	 * @return Function to find partition per record
	 */
	default Function<K, Integer> getPartitioner() {
		return key -> {
			String partitionKey = getPartitionSelector().apply(key);
			return toPositive(murmur2(partitionKey.getBytes(StandardCharsets.UTF_8))) % getPartitions();
		};
	}

	/**
	 * The number of partitions this topic has. Defaults to 15.
	 * 
	 * @return number of partitions
	 */
	default int getPartitions() {
		return 15;
	}

	/**
	 * How many times a KafkaProducer will retry sending a batch of messages
	 * before throwing an exception for this topic
	 * 
	 * @return the number of producer retries
	 */
	default int getProducerRetries() {
		return 10;
	}

	/**
	 * How many replicas (including the partition leader) have to acknowledge a
	 * message in order for the producer to consider it as sent
	 * <p>
	 * We set it to <code>all</code> in order to leave the broker decide through
	 * <code>min.insync.replicas</code> configuration
	 * 
	 * @return Acknowledge policy
	 */
	default String getProducerAcknowledgements() {
		return "all";
	}

}
