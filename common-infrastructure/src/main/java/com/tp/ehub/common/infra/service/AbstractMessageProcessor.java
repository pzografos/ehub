package com.tp.ehub.common.infra.service;

import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.infra.messaging.kafka.receiver.KafkaReactiveReceiver;

import reactor.core.scheduler.Scheduler;

public abstract class AbstractMessageProcessor<K, M extends Message<K>> implements Consumer<M> {

	@Inject
	KafkaReactiveReceiver receiver;
	
	private String consumerId;

	private Class<M> messageClass;
	
	public AbstractMessageProcessor(String consumerId, Class<M> messageClass) {
		this.consumerId = consumerId;
		this.messageClass = messageClass;
	}

	public final void run(Scheduler productScheduler) {
		
		receiver.setConsumerId(consumerId);
		
		receiver.receive(messageClass) 
				.subscribeOn(productScheduler)
				.subscribe(this);
	}
	
	@Override
	public abstract void accept(M message);

}
