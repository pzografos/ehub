package com.tp.ehub.common.infra.messaging.kafka;

import static java.util.UUID.randomUUID;

import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.store.PartitionedMessageStore;
import com.tp.ehub.common.infra.messaging.kafka.receiver.KafkaBlockingReceiver;
import com.tp.ehub.common.infra.messaging.kafka.sender.KafkaBlockingSender;

@ApplicationScoped
public class KafkaMessageStore implements PartitionedMessageStore {

	@Inject
	KafkaBlockingReceiver receiver;

	@Inject
	KafkaBlockingSender sender;
			
	@Override
	public <K, M extends Message<K>> Stream<M> getbyKey(K key, Class<M> messageClass) {
		receiver.setConsumerId(randomUUID().toString());
		return receiver.getByKey(key, messageClass);
	}
	
	@Override
	public <K, M extends Message<K>> Stream<M> getbyKey(K key, String partitionKey, Class<M> messageClass) {
		receiver.setConsumerId(randomUUID().toString());
		return receiver.getByKeyAndPartition(key, messageClass, partitionKey);
	}

	@Override
	public <K, M extends Message<K>> void publish(Stream<M> messages, Class<M> messageClass) {
		sender.send(messages, messageClass);
	}

}
