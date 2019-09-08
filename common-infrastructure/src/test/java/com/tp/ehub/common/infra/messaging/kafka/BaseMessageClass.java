package com.tp.ehub.common.infra.messaging.kafka;

import java.util.UUID;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.Container;

@Container( name = "base-message-topic", keyClass = UUID.class)
abstract class BaseMessageClass implements Message<UUID>{

}
