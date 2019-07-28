package com.tp.ehub.common.domain.model;

import java.util.stream.Stream;

import com.tp.ehub.common.domain.messaging.Event;

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
 * @param <K>
 *            The type of the aggregate's unique identifier 
 * @param <E> 
 * 			  The type of the <code>Event</code> that the aggregate is made of
 * @param <T>
 *            The type of the aggregate's root entity
 */
public interface Aggregate<K, E extends Event<K>, T extends Entity> extends Versionable{

	/**
	 * Get the root entity
	 * 
	 * @return the root entity
	 */
	T getRoot();
	
	/**
	 * Get the aggregate key
	 * 
	 * @return the aggregate key
	 */
	K getKey();
	
	/**
	 * 
	 * @return the events to publish
	 */
	Stream<E> getNewEvents();

}
