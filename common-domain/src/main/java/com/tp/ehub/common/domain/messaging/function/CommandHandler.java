package com.tp.ehub.common.domain.messaging.function;

import java.util.Collection;
import java.util.function.BiFunction;

import com.tp.ehub.common.domain.messaging.Command;
import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;

public interface CommandHandler<K1, C extends Command<K1>, K2, E extends Event<K2>, T extends Entity, A extends Aggregate<K2, E, T>> extends BiFunction<A, C, Collection<E>> {
	
}
