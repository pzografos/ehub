package com.tp.ehub.common.types;

/**
 * The <code>Aggregate</code> represents a partial snapshot of the system's
 * current state to be used by the system for implementing business rules.
 * 
 * @param <E> the type of the events this Aggregate is made of
 * @param <T> the type of the underlying AggregateEntity
 * @param <K> the type of this underlying entity's unique identifier
 */
public interface Aggregate<E extends Event, T extends Entity<K>, K> {

	/**
	 * Get the root entity 
	 * 
	 * @return the root entity
	 */
	T root();
	
	/**
	 * Constructs the aggregate data from merging the last consolidated aggregation
	 * and events that have arrived after that. If no aggregate is found in the
	 * database a new one is created.
	 * 
	 * @param id the unique identifier of the aggregate
	 */
	void load(K id);

	/**
	 * Applies a new Event to the aggregate
	 * 
	 * @param E the type of event to apply
	 */
	void apply(E event);

	/**
	 * Saves the aggregate in the database and sends the new events to the event
	 * store
	 */
	void publish();
}
