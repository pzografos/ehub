package com.tp.ehub.common.domain.messaging;

import java.io.Serializable;
import java.time.ZonedDateTime;

public interface Message<K> extends Serializable {

	ZonedDateTime getTimestamp();
	
	/**
	 * Gets the event key
	 * 
	 * @return the event key
	 */
	K getKey();
}