package com.tp.ehub.product.messaging;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.tp.ehub.messaging.kafka.KafkaCluster;
import com.tp.ehub.messaging.kafka.Topic;
import com.tp.ehub.messaging.kafka.service.Receiver;
import com.tp.ehub.messaging.kafka.service.SinglePartitionKafkaReceiver;
import com.tp.ehub.product.model.event.ProductEvent;

@ApplicationScoped
@Receiver("product-events")
public class SinglePartitionProductEventsReceiver extends SinglePartitionKafkaReceiver<UUID, ProductEvent> {

	@Inject
	public SinglePartitionProductEventsReceiver(@Named("product-events") Topic<UUID, ProductEvent> topic, KafkaCluster kafka) {
		super(kafka, topic);
	}

}
