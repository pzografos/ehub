package com.tp.ehub.messaging.kafka;

import com.tp.ehub.model.messaging.Message;
import com.tp.ehub.model.messaging.MessageRecord;

public class KafkaRecord<K, M extends Message> implements MessageRecord<K, M> {
	
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
