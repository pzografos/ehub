package com.tp.ehub.common.infra.messaging.kafka.receiver;

import java.util.function.Function;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.KeyValueMessageContainer;

class RecordTransformer<K, M extends Message<K>> implements Function<ConsumerRecord<String, byte[]>, M> {

	private KeyValueMessageContainer<K, M> topic;

	public RecordTransformer(KeyValueMessageContainer<K, M> topic) {
		this.topic = topic;
	}

	@Override
	public M apply(ConsumerRecord<String, byte[]> record) {
		M message = topic.getValueDeserializer().apply(record.value());
		return message;
	}

}
