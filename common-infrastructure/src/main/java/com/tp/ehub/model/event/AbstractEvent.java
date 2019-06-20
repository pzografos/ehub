package com.tp.ehub.model.event;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tp.ehub.serialization.JsonMessage;

/**
 * Base event implementation
 *
 */
@JsonMessage
public abstract class AbstractEvent<K> implements Event<K> {

	protected K key;

	protected ZonedDateTime timestamp;
	
	protected Long version;
	
	@Override
	@JsonIgnore
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
}
