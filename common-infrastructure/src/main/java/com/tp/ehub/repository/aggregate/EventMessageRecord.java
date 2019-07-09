package com.tp.ehub.repository.aggregate;

import com.tp.ehub.model.event.Event;
import com.tp.ehub.model.messaging.MessageRecord;

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
