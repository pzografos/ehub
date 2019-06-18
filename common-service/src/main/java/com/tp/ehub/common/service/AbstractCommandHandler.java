package com.tp.ehub.common.service;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.inject.Inject;

import com.tp.ehub.common.types.Aggregate;
import com.tp.ehub.common.types.Command;
import com.tp.ehub.common.types.CommandHandler;
import com.tp.ehub.common.types.Entity;
import com.tp.ehub.common.types.Event;

public abstract class AbstractCommandHandler<E extends Event, T extends Entity<K>, K> implements CommandHandler {

	@Inject
	private Aggregate<E, T, K> aggregate;

	public final void accept(Command command) {
		aggregate.load(aggregateKey().apply(command));
		try {
			commandToEvents().apply(command, aggregate.root()).stream().forEach(aggregate::apply);
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
	 * @return the events to apply
	 */
	public abstract BiFunction<Command, T, Collection<E>> commandToEvents();
	
	public abstract Function<Command, K> aggregateKey();
}
