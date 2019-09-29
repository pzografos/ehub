package com.tp.ehub.common.infra.messaging.kafka;

import static java.time.Duration.ofMillis;
import static java.util.Collections.singleton;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.KeyValueMessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

public abstract class AbstractMessageProcessor<K, M extends Message<K>> {
	
	@Inject
	KafkaCluster kafka;
	
	@Inject
	MessageContainerRegistry registry;
	
	private String consumerId;

	private Class<M> messageClass;
	
	private Duration pollingInterval;
	
	public AbstractMessageProcessor(String consumerId, Class<M> messageClass) {
		this(consumerId, messageClass, ofMillis(500L));
	}

	public AbstractMessageProcessor(String consumerId, Class<M> messageClass, Duration pollingInterval) {
		this.consumerId = consumerId;
		this.messageClass = messageClass;
		this.pollingInterval = pollingInterval;
	}

	public final void run(Scheduler productScheduler) {
		
		KeyValueMessageContainer<K, M> topic = (KeyValueMessageContainer<K, M>) registry.get(messageClass);
		
        KafkaReceiver<String, byte[]> receiver = getReceiver(topic);
        
        KafkaSender<String, byte[]> sender = getSender();

		receiver.receiveExactlyOnce(sender.transactionManager())
        		.concatMap(f -> f.map(r -> topic.getValueDeserializer().apply(r.value())))
        		.doOnNext(this::process)
        		.concatWith(sender.transactionManager().commit())
				.subscribeOn(productScheduler)
				.onErrorResume(e -> sender.transactionManager().abort().then(Mono.error(e)))
			    .doOnCancel(() -> sender.close())
				.subscribe();
	}
	
	public abstract void process(M message);
	
	protected KafkaSender<String, byte[]> getSender() {
		
		Map<String, Object> producerProps = Map.ofEntries(new AbstractMap.SimpleEntry<>(ProducerConfig.RETRIES_CONFIG, 10),
				new AbstractMap.SimpleEntry<>(ProducerConfig.ACKS_CONFIG, "all"),
				new AbstractMap.SimpleEntry<>(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()),
				new AbstractMap.SimpleEntry<>(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "KafkaExactlyOnce"),
				new AbstractMap.SimpleEntry<>(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class),
				new AbstractMap.SimpleEntry<>(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class));
		
		SenderOptions<String, byte[]> senderOptions = SenderOptions.<String, byte[]> create(producerProps);
		senderOptions.stopOnError(true);
		
		return KafkaSender.create(senderOptions);
	}

	private KafkaReceiver<String, byte[]> getReceiver(KeyValueMessageContainer<K, M> topic) {
		
		Map<String, Object> consumerProps = Map.ofEntries(new AbstractMap.SimpleEntry<>(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int) pollingInterval.toMillis()),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBrokers()), 
				new AbstractMap.SimpleEntry<>(ConsumerConfig.GROUP_ID_CONFIG, consumerId),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class),
				new AbstractMap.SimpleEntry<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class));
		
		ReceiverOptions<String, byte[]> options =  ReceiverOptions.<String, byte[]> create(consumerProps)
                .consumerProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed")
				.subscription(singleton(topic.getName()));
        
		return KafkaReceiver.create(options);
	}
	
}
