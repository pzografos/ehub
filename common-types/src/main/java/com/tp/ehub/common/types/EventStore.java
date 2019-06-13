package com.tp.ehub.common.types;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Draft implementation please ignore in review. 
 *
 * @param <E>
 */
public interface EventStore<E extends Event> {

	Stream<E> events(Predicate<E> filter);
	
	void publish(Collection<E> events);
}
