package com.tp.ehub.product.repository;

import com.tp.ehub.common.infra.messaging.kafka.AbstractMessageStore;
import com.tp.ehub.common.infra.messaging.kafka.receiver.Receiver;
import com.tp.ehub.common.infra.messaging.kafka.receiver.SinglePartitionKafkaReceiver;
import com.tp.ehub.common.infra.messaging.kafka.sender.Sender;
import com.tp.ehub.common.infra.messaging.kafka.sender.TopicKafkaSender;
import com.tp.ehub.product.model.event.ProductEvent;

import javax.inject.Inject;
import java.util.UUID;

public class ProductEventsStore extends AbstractMessageStore<UUID, ProductEvent> {

	@Inject
	public ProductEventsStore(@Sender("product-events") TopicKafkaSender<UUID, ProductEvent> sender,
			@Receiver("product-events") SinglePartitionKafkaReceiver<UUID, ProductEvent> receiver) {
		super(sender, receiver);
	}

}
