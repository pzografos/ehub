package com.tp.ehub.common.domain.function;

import java.util.function.BiFunction;

/**
 * Reduces <S> based on the value of <A>
 * 
 * @param <S> the type which will be reduced
 * @param <A> the type based on which reduction will take place
 */
@FunctionalInterface
public interface Reducer<S, A> extends BiFunction<S, A, S> {

}