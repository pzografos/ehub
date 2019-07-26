package com.tp.ehub.common.infra.messaging.kafka;

import static org.apache.kafka.common.utils.Utils.murmur2;
import static org.apache.kafka.common.utils.Utils.toPositive;

import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;

import com.tp.ehub.common.domain.messaging.container.MessageContainer;

@ApplicationScoped
public class Partitioner {

	public Integer getPartition(String partitionKey, MessageContainer<?, ?> container) {
		return toPositive(murmur2(partitionKey.getBytes(StandardCharsets.UTF_8))) % container.getPartitions();
	}

}
