package com.tp.ehub.common.domain.function;

import com.tp.ehub.common.domain.exception.BusinessException;

/**
 * Adds a business exception in the signature of <code>java.util.Consumer</code> 
 *
 * @param <T> the type of the argument to the function
 */
public interface CheckedConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t) throws BusinessException;
}
