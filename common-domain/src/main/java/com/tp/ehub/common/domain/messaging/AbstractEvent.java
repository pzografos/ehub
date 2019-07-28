package com.tp.ehub.common.domain.messaging;

import java.time.ZonedDateTime;

/**
 * Base event implementation
 *
 */
@JsonMessage
public abstract class AbstractEvent<K> implements Event<K> {

	protected ZonedDateTime timestamp;
	
	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
