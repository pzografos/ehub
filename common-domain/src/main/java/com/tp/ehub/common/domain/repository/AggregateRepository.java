package com.tp.ehub.common.domain.repository;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;

public interface AggregateRepository<K, E extends Event<K>, T extends Entity, A extends Aggregate<K, E, T>> {

	A get(K key);

	void save(A aggregate);
}
