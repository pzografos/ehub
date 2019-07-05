package com.tp.ehub.repository;

import com.tp.ehub.model.aggregate.Aggregate;
import com.tp.ehub.model.entity.Entity;
import com.tp.ehub.model.event.Event;

public interface AggregateRepository<A extends Aggregate<E, T, K>, E extends Event<K>, T extends Entity<K>, K> {

	A get(K id);

	void save(A aggregate);
}
