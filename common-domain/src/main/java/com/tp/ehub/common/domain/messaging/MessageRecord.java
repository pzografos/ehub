package com.tp.ehub.common.domain.messaging;

/**
 * A record of a <code>Message<code> that can be found inside a
 * <code>MessageContainer<code>
 *
 * @param <K> the type of the message key
 * @param <M> the type of the message
 */
public interface MessageRecord<K, M extends Message<K>> {

	/**
	 * @return the message key
	 */
	K getKey();

	/**
	 * @return the message contained in the record
	 */
	M getMessage();

}
