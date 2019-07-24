package com.tp.ehub.common.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import com.tp.ehub.common.domain.messaging.Event;

/**
 * Base implementation for the <code>Aggregate</code>.
 *
 * @param <E>
 *            The type of the <code>Event</code> that the aggregate is made of
 * @param <T>
 *            The type of the aggregate's root entity
 * @param <K>
 *            The type of the aggregate's root entity unique identifier
 */
public abstract class AbstractAggregate<E extends Event<K>, T extends Entity<K>, K> implements Aggregate<E, T, K> {

	protected T rootEntity;

	protected Collection<E> newEvents = new ArrayList<>();

	protected AbstractAggregate(T rootEntity) {
		this.rootEntity = rootEntity;
	}

	@Override
	public T getRoot() {
		return this.rootEntity;
	}

	@Override
	public final Stream<E> getNewEvents() {
		return newEvents.stream();
	}
}