package com.tp.ehub.common.infra.repository;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.messaging.MessageRecord;

class EventMessageRecord<K, E extends Event<K>> implements MessageRecord<K, E> {

	private K key;
	
	private E message;

	public EventMessageRecord(E event) {
		key = event.getKey();
		message = event;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public E getMessage() {
		return message;
	}

}
