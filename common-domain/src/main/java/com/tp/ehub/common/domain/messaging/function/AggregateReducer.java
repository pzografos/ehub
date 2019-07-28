package com.tp.ehub.common.domain.messaging.function;

import java.util.Collection;
import java.util.function.BiFunction;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;

public interface AggregateReducer<K, E extends Event<K>, T extends Entity, A extends Aggregate<K, E, T>> extends BiFunction<A, E, A> {

	default public A apply(A aggregate, Collection<E> events) {
		A result = aggregate;
		for(E event:events) {
			result = this.apply(result, event);
		}
		return result;
	}
}
