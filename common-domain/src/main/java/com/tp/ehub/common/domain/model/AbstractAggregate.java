package com.tp.ehub.common.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import com.tp.ehub.common.domain.messaging.Event;

/**
 * Base implementation for the <code>Aggregate</code>.
 *
 * @param <K>
 *            The type of the aggregate's root entity unique identifier 
 * @param <E>
 *            The type of the <code>Event</code> that the aggregate is made of
 * @param <T>
 *            The type of the aggregate's root entity 
 */
public abstract class AbstractAggregate<K, E extends Event<K>, T extends Entity> implements Aggregate<K, E, T> {

	protected T rootEntity;
	
	protected K key;

	protected Collection<E> newEvents = new ArrayList<>();
	
	protected AbstractAggregate(K key, T rootEntity) {
		this.key = key;
		this.rootEntity = rootEntity;
	}

	@Override
	public T getRoot() {
		return this.rootEntity;
	}

	public void setRootEntity(T rootEntity) {
		this.rootEntity = rootEntity;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public final Stream<E> getNewEvents() {
		return newEvents.stream();
	}
	
	public void addEvent(E event) {
		newEvents.add(event);
	}
	
	public void addEvents(Collection<E> events) {
		newEvents.addAll(events);
	}
}