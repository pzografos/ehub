package com.tp.ehub.model.aggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import com.tp.ehub.model.entity.Entity;
import com.tp.ehub.model.event.Event;

/**
 * Base implementation for the <code>Aggregate</code>.
 *
 * @param <E> The type of the <code>Event</code> that the aggregate is made of
 * @param <T> The type of the aggregate's root entity
 * @param <K> The type of the aggregate's root entity unique identifier
 */
public abstract class AbstractAggregate<E extends Event<K>, T extends Entity<K>, K> implements Aggregate<E, T, K> {

	private T rootEntity;

	private Collection<E> newEvents = new ArrayList<>();
	
	protected AbstractAggregate(T rootEntity) {
		this.rootEntity = rootEntity;
	}
	
	@Override
	public T getRoot() {
		return this.rootEntity;
	}

	@Override
	public final void apply(E event) {
		rootEntity = mutate(rootEntity, event);
		newEvents.add(event);
	}

	@Override
	public final Stream<E> getNewEvents(){
		return newEvents.stream();
	}

	/**
	 * Applies an event to the root entity
	 * 
	 * @param entity the previous state of the root entity
	 * @param event  the event to apply
	 * 
	 * @return the updated entity
	 */
	protected abstract T mutate(T entity, E event);

}