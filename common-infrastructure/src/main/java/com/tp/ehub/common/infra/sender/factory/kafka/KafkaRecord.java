package com.tp.ehub.common.infra.sender.factory.kafka;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.MessageRecord;

public class KafkaRecord<K, M extends Message<K>> implements MessageRecord<K, M> {

	private K key;

	private M message;

	private Integer partition;

	private Long offset;

	public KafkaRecord(K key, M message) {
		this.key = key;
		this.message = message;
	}

	public KafkaRecord(K key, M message, Integer partition, Long offset) {
		this.key = key;
		this.message = message;
		this.partition = partition;
		this.offset = offset;
	}

	@Override
	public K getKey() {
		return this.key;
	}

	@Override
	public M getMessage() {
		return this.message;
	}

	public Integer getPartition() {
		return partition;
	}

	public Long getOffset() {
		return offset;
	}

}
