package com.tp.ehub.common.infra.service;

import java.util.Collection;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.Command;
import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.messaging.function.AggregateReducer;
import com.tp.ehub.common.domain.messaging.function.CommandHandler;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;
import com.tp.ehub.common.domain.repository.PartitionedAggregateRepository;

public abstract class AbstractCommandProcessor<K1, C extends Command<K1>, K2, E extends Event<K2>, T extends Entity, A extends Aggregate<K2, E, T>> extends AbstractMessageProcessor<K1, C>{

	@Inject
	PartitionedAggregateRepository<K2, E, T, A> aggregateRepository;
	
	@Inject
	CommandHandler<K1, C, K2, E, T, A> commandHandler;
	
	@Inject
	AggregateReducer <K2, E, T, A> aggregateEventReducer;
	
	protected AbstractCommandProcessor(String consumerId, Class<C> messageClass) {
		super(consumerId, messageClass);
	}

	@Override
	public void accept(C command) {
		A aggregate = aggregateRepository.get(getAggregateKey(command), getAggregatePartitionKey(command));
		Collection<E> events = commandHandler.apply(aggregate, command);
		aggregate = aggregateEventReducer.apply(aggregate, events);
		aggregateRepository.save(aggregate);		
	}
	
	protected abstract String getAggregatePartitionKey(C command);
	
	protected abstract K2 getAggregateKey(C command);

}
