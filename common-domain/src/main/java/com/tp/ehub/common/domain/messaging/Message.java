package com.tp.ehub.common.domain.messaging;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Interface defining a message in the system
 *
 * @param <K> the message key
 */
public interface Message<K> extends Serializable {

	/**
	 * The time the message was created. 
	 * 
	 * @return the message timestamp
	 */
	ZonedDateTime getTimestamp();
	
	/**
	 * Gets the event key
	 * 
	 * @return the event key
	 */
	K getKey();
}