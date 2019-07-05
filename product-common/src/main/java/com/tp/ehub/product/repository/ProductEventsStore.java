package com.tp.ehub.product.repository;

import com.tp.ehub.messaging.kafka.service.Receiver;
import com.tp.ehub.messaging.kafka.service.Sender;
import com.tp.ehub.messaging.kafka.service.SinglePartitionKafkaReceiver;
import com.tp.ehub.messaging.kafka.service.TopicKafkaSender;
import com.tp.ehub.product.model.event.ProductEvent;
import com.tp.ehub.repository.message.AbstractMessageStore;

import javax.inject.Inject;
import java.util.UUID;

public class ProductEventsStore extends AbstractMessageStore<UUID, ProductEvent> {

	@Inject
	public ProductEventsStore(@Sender("product-events") TopicKafkaSender<UUID, ProductEvent> sender,
			@Receiver("product-events") SinglePartitionKafkaReceiver<UUID, ProductEvent> receiver) {
		super(sender, receiver);
	}

}
