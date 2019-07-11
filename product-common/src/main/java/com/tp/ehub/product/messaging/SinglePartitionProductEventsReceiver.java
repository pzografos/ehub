package com.tp.ehub.product.messaging;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Topic;
import com.tp.ehub.common.infra.messaging.kafka.receiver.Receiver;
import com.tp.ehub.common.infra.messaging.kafka.receiver.SinglePartitionKafkaReceiver;
import com.tp.ehub.product.model.event.ProductEvent;

@ApplicationScoped
@Receiver("product-events")
public class SinglePartitionProductEventsReceiver extends SinglePartitionKafkaReceiver<UUID, ProductEvent> {

	@Inject
	public SinglePartitionProductEventsReceiver(@Named("product-events") Topic<UUID, ProductEvent> topic, KafkaCluster kafka) {
		super(kafka, topic);
	}

}
