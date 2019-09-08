package com.tp.ehub.common.infra.service;

import java.util.Collection;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.exception.BusinessException;
import com.tp.ehub.common.domain.messaging.Command;
import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.messaging.function.AggregateReducer;
import com.tp.ehub.common.domain.messaging.function.CommandHandler;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;
import com.tp.ehub.common.domain.repository.PartitionedAggregateRepository;
import com.tp.ehub.common.infra.request.RequestHandler;

public abstract class AbstractCommandProcessor<K1, C extends Command<K1>, K2, E extends Event<K2>, T extends Entity, A extends Aggregate<K2, E, T>> extends AbstractMessageProcessor<K1, C>{

	@Inject
	PartitionedAggregateRepository<K2, E, T, A> aggregateRepository;
	
	@Inject
	CommandHandler<K1, C, K2, E, T, A> commandHandler;
	
	@Inject
	AggregateReducer <K2, E, T, A> aggregateEventReducer;
	
	@Inject
	RequestHandler requestHandler;
	
	@Inject
	Logger log;
	
	protected AbstractCommandProcessor(String consumerId, Class<C> messageClass) {
		super(consumerId, messageClass);
	}

	@Override
	public void accept(C command) {
		log.info("Processing command {}", command);
		A aggregate = aggregateRepository.get(getAggregateKey(command), getAggregatePartitionKey(command));
		try {
			Collection<E> events = commandHandler.apply(aggregate, command);
			log.info("Adding events {}", events.size());
			aggregate = aggregateEventReducer.apply(aggregate, events);
			aggregateRepository.save(aggregate);
			requestHandler.acceptRequest(command.getRequestId());
		} catch (BusinessException e) {
			requestHandler.failRequest(command.getRequestId(), e.getMessage());
		}
		
	}
	
	protected abstract String getAggregatePartitionKey(C command);
	
	protected abstract K2 getAggregateKey(C command);

}
