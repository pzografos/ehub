package com.tp.ehub.common.domain.messaging.function;

import java.util.Collection;
import java.util.function.BiFunction;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;

public interface EventHandler<K1, E1 extends Event<K1>, K2, E2 extends Event<K2>, T extends Entity, A extends Aggregate<K2, E2, T>> extends BiFunction<A, E1, Collection<E2>> {
	
}