package com.tp.ehub.common.domain.repository;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;

public interface PartitionedAggregateRepository<A extends Aggregate<E, T, K>, E extends Event<K>, T extends Entity<K>, K> extends AggregateRepository<A,E,T,K>{

	A get(K id, String partitionKey);
}
