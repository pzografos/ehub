package com.tp.ehub.common.service;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.inject.Inject;

import com.tp.ehub.common.types.Aggregate;
import com.tp.ehub.common.types.AggregateEntity;
import com.tp.ehub.common.types.Command;
import com.tp.ehub.common.types.CommandHandler;
import com.tp.ehub.common.types.Event;

public abstract class AbstractCommandHandler<C extends Command, E extends Event, T extends AggregateEntity<K>, K>
		implements CommandHandler<C> {

	@Inject
	private Aggregate<E, T, K> aggregate;

	public final void accept(C command) {
		aggregate.load(aggregateKey().apply(command));
		try {
			commandToEvents().apply(command, aggregate.root()).stream().forEach(aggregate::apply);
			aggregate.publish();
		} catch (IllegalArgumentException e) {
			handleError(command);
		}
	}
	
	@Override
	public final void handleError(C command) {
		throw new RuntimeException(String.format("Could not apply command {}", command));
	}

	/**
	 * This function accepts the root entity of the aggregate and the command and
	 * produces the events to apply on the aggregate. It is responsible for
	 * implementing all the validations and the actual business logic.
	 * 
	 * @return the events to apply
	 */
	public abstract BiFunction<C, T, Collection<E>> commandToEvents();
	
	public abstract Function<C, K> aggregateKey();
}
