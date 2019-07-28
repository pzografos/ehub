package com.tp.ehub.common.infra.service;

import java.util.Collection;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.messaging.function.AggregateReducer;
import com.tp.ehub.common.domain.messaging.function.EventHandler;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;
import com.tp.ehub.common.domain.repository.PartitionedAggregateRepository;

public abstract class AbstractEventProcessor<K1, E1 extends Event<K1>, K2, E2 extends Event<K2>, T extends Entity, A extends Aggregate<K2, E2, T>> extends AbstractMessageProcessor<K1, E1>{

	@Inject
	PartitionedAggregateRepository<K2, E2, T, A> aggregateRepository;
	
	@Inject
	EventHandler<K1, E1, K2, E2, T, A> eventHandler;
	
	@Inject
	AggregateReducer <K2, E2, T, A> aggregateEventReducer;
	
	protected AbstractEventProcessor(String consumerId, Class<E1> messageClass) {
		super(consumerId, messageClass);
	}

	@Override
	public void accept(E1 event) {
		A aggregate = aggregateRepository.get(getAggregateKey(event), getAggregatePartitionKey(event));
		Collection<E2> events = eventHandler.apply(aggregate, event);
		aggregate = aggregateEventReducer.apply(aggregate, events);
		aggregateRepository.save(aggregate);		
	}
	
	protected abstract String getAggregatePartitionKey(E1 event);
	
	protected abstract K2 getAggregateKey(E1 event);

}
