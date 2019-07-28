package com.tp.ehub.common.infra.service;

import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.infra.messaging.kafka.receiver.KafkaTopicReceiver;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public abstract class AbstractMessageProcessor<K, M extends Message<K>> implements Consumer<M> {

	@Inject
	KafkaTopicReceiver receiver;
	
	private String consumerId;

	private Class<M> messageClass;
	
	public AbstractMessageProcessor(String consumerId, Class<M> messageClass) {
		this.consumerId = consumerId;
		this.messageClass = messageClass;
	}

	public final void run(Scheduler productScheduler) {
		
		receiver.setConsumerId(consumerId);
		
		final Flux<M> commandsFlux = receiver.receiveAll(messageClass) 
				.subscribeOn(productScheduler);
		commandsFlux.subscribe(this);
	}
	
	@Override
	public abstract void accept(M message);

}
