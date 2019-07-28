package com.tp.ehub.common.domain.messaging.function;

import java.util.function.BiFunction;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.model.View;

public interface ViewReducer<K1, E extends Event<K1>, K2, V extends View<K2>> extends BiFunction<V, E, V> {

}
