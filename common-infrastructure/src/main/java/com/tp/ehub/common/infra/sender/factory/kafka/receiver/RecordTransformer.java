package com.tp.ehub.common.infra.sender.factory.kafka.receiver;

import java.util.function.Function;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.MessageContainer;
import com.tp.ehub.common.infra.sender.factory.kafka.KafkaRecord;

class RecordTransformer<K, M extends Message<K>> implements Function<ConsumerRecord<String, byte[]>, KafkaRecord<K, M>> {

	private MessageContainer<K, M> topic;

	public RecordTransformer(MessageContainer<K, M> topic) {
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
