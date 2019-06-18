package com.tp.ehub.common.service;

import java.util.Collection;

import javax.inject.Inject;

import com.tp.ehub.common.aggregate.Aggregate;
import com.tp.ehub.common.event.Event;
import com.tp.ehub.common.model.Command;
import com.tp.ehub.common.model.Entity;

public abstract class AbstractCommandHandler<E extends Event, T extends Entity<K>, K> implements CommandHandler {

	@Inject
	private Aggregate<E, T, K> aggregate;

	public final void accept(Command command) {
		aggregate.load(aggregateKey(command));
		try {
			events(aggregate.root(), command).stream().forEach(aggregate::apply);
			aggregate.publish();
		} catch (IllegalArgumentException e) {
			handleError(command);
		}
	}
	
	@Override
	public final void handleError(Command command) {
		throw new RuntimeException(String.format("Could not apply command {}", command));
	}

	/**
	 * This function accepts the root entity of the aggregate and the command and
	 * produces the events to apply on the aggregate. It is responsible for
	 * implementing all the validations and the actual business logic.
	 * 
	 * @param rootEntity the root entity
	 * @param the command to apply
	 * @return the events to apply
	 */
	public abstract Collection<E> events(T rootEntity, Command command);
	
	public abstract K aggregateKey(Command command);
}
