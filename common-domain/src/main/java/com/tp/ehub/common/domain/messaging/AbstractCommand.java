package com.tp.ehub.common.domain.messaging;

import java.time.ZonedDateTime;
import java.util.UUID;

@JsonMessage
public abstract class AbstractCommand<K> implements Command<K>{

	protected ZonedDateTime timestamp;
	
	protected UUID requestId;
	
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

}
