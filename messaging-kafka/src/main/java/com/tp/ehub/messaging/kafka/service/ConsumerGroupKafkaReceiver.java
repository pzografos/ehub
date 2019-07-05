package com.tp.ehub.messaging.kafka.service;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.tp.ehub.messaging.kafka.KafkaCluster;
import com.tp.ehub.messaging.kafka.Topic;
import com.tp.ehub.model.messaging.Message;
import com.tp.ehub.model.messaging.MessageRecord;
import com.tp.ehub.service.messaging.GlobalMessageReceiver;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

public class ConsumerGroupKafkaReceiver<K, M extends Message> implements GlobalMessageReceiver<K,M>{


	private KafkaCluster kafka;
	
	private Topic<K,M> topic;
		
	public ConsumerGroupKafkaReceiver(KafkaCluster kafka, Topic<K, M> topic) {
		this.kafka = kafka;
		this.topic = topic;
	}

	@Override
	public Flux<MessageRecord<K, M>> receive(String groupId, boolean fromStart) {
		ReceiverOptions<String, byte[]> receiverOptions = ReceiverOptions.<String, byte[]>create(consumerProps(groupId, fromStart))
				.subscription(Collections.singleton(topic.getName()));
		RecordTransformer<K,M> transformer = new RecordTransformer<K,M>(topic);
		KafkaReceiver<String, byte[]> receiver = KafkaReceiver.create(receiverOptions);
		return receiver.receiveAtmostOnce().map(transformer);
	}
		
	protected Map<String, Object> consumerProps(String groupId, boolean fromStart){
		return Map.ofEntries(
				new AbstractMap.SimpleEntry<>(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, String.valueOf(500L)), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.GROUP_ID_CONFIG, groupId), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class));
	}

}
