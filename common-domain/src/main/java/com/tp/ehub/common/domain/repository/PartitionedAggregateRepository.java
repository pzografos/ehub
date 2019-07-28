package com.tp.ehub.common.domain.repository;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;

public interface PartitionedAggregateRepository<K, E extends Event<K>, T extends Entity, A extends Aggregate<K, E, T>> extends AggregateRepository<K, E, T, A> {

	A get(K key, String partitionKey);
}
