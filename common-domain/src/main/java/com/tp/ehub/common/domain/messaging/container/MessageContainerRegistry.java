package com.tp.ehub.common.domain.messaging.container;

import com.tp.ehub.common.domain.messaging.Message;

/**
 * A message container registry performs recovery operations for message
 * container based on properties that should be unique across the application.
 *
 */
public interface MessageContainerRegistry {

	/**
	 * Get the message container that contains messages of the given type
	 * 
	 * @param <K>
	 *            the type of message key
	 * @param <M>
	 *            the type of message
	 * @param type
	 *            the exact implementing class of the message
	 * @return the message container
	 */
	public <K, M extends Message<K>> MessageContainer<K, M> get(Class<M> type);
}
