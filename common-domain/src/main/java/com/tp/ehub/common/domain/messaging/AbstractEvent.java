package com.tp.ehub.common.domain.messaging;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Base event implementation
 *
 */
@JsonMessage
public abstract class AbstractEvent<K> implements Event<K> {

	protected ZonedDateTime timestamp;
	
	protected UUID requestId;
	
	protected Long version;
	
	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public UUID getRequestId() {
		return requestId;
	}

	public void setRequestId(UUID requestId) {
		this.requestId = requestId;
	}
	
	@Override
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
