package com.tp.ehub.messaging.kafka.service;

import java.util.function.Function;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tp.ehub.messaging.kafka.KafkaRecord;
import com.tp.ehub.messaging.kafka.Topic;
import com.tp.ehub.model.messaging.Message;

class RecordTransformer<K, M extends Message> implements Function<ConsumerRecord<String, byte[]>, KafkaRecord<K, M>> {

	private Topic<K, M> topic;

	public RecordTransformer(Topic<K, M> topic) {
		this.topic = topic;
	}

	@Override
	public KafkaRecord<K, M> apply(ConsumerRecord<String, byte[]> record) {
		K key = topic.getKeyDeserializer().apply(record.key());
		M message = topic.getValueDeserializer().apply(record.value());
		Integer partition = record.partition();
		Long offset = record.offset();
		return new KafkaRecord<K, M>(key, message, partition, offset);
	}

}
