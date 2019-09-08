package com.tp.ehub.common.infra.messaging.kafka;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.Container;

@Container( name = "simple-message-topic", keyClass = UUID.class)
class SimpleMessage implements Message<UUID>{

	@Override
	public ZonedDateTime getTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
