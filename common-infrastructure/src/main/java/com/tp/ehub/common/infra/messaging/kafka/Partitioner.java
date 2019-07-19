package com.tp.ehub.common.infra.messaging.kafka;

import static org.apache.kafka.common.utils.Utils.murmur2;
import static org.apache.kafka.common.utils.Utils.toPositive;

import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Partitioner {

	public Integer getPartition(String partitionKey) {
		return toPositive(murmur2(partitionKey.getBytes(StandardCharsets.UTF_8))) % getPartitions();
	}

	/**
	 * The number of partitions this conatainer. Defaults to 15.
	 * 
	 * @return number of partitions
	 */
	protected int getPartitions() {
		return 15;
	}
}
