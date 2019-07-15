package com.tp.ehub.common.domain.repository;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;

public interface AggregateRepository<A extends Aggregate<E, T, K>, E extends Event<K>, T extends Entity<K>, K> {

	A get(K id);

	void save(A aggregate);
}
