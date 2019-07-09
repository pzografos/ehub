package com.tp.ehub.model.aggregate;

import java.util.stream.Stream;

import com.tp.ehub.model.entity.Entity;
import com.tp.ehub.model.event.Event;

/**
 * Based on the DDD definition the <code>Aggregate<code> represents a cluster 
 * of domain objects that can be treated as a single unit. 
 * 
 * <p>
 * For this system, the <code>Aggregate<code> is made of two types of objects:
 * <ul>
 * <li>A root entity that represents a consolidated snapshot of the system's events</li>
 * <li>A collection of events that have been applied to the root entity but have not yet been published</li>
 * </ul>
 * </p>
 * 
 * @param <E> 
 * 			  The type of the <code>Event</code> that the aggregate is made of
 * @param <T>
 *            The type of the aggregate's root entity
 * @param <K>
 *            The type of the aggregate's root entity unique identifier
 */
public interface Aggregate<E extends Event<K>, T extends Entity<K>, K> {

	/**
	 * Get the root entity
	 * 
	 * @return the root entity
	 */
	T getRoot();

	/**
	 * Applies a new Event to the aggregate
	 * 
	 * @param event
	 *            the event to apply
	 */
	void apply(E event);

	/**
	 * 
	 * @return the events to publish
	 */
	Stream<E> getNewEvents();

}
