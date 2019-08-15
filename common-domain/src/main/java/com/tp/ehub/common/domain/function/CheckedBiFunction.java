package com.tp.ehub.common.domain.function;

import com.tp.ehub.common.domain.exception.BusinessException;

/**
 * Adds a business exception in the signature of <code>java.util.BiFunction</code> 
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 */
public interface CheckedBiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u) throws BusinessException;
}
