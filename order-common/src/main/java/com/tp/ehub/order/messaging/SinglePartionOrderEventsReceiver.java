package com.tp.ehub.order.messaging;

import com.tp.ehub.messaging.kafka.KafkaCluster;
import com.tp.ehub.messaging.kafka.Topic;
import com.tp.ehub.messaging.kafka.service.Receiver;
import com.tp.ehub.messaging.kafka.service.SinglePartitionKafkaReceiver;
import com.tp.ehub.order.model.event.OrderEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

@ApplicationScoped
@Receiver("order-events")
public class SinglePartionOrderEventsReceiver extends SinglePartitionKafkaReceiver<UUID, OrderEvent> {

	@Inject
	public SinglePartionOrderEventsReceiver(@Named("order-events") Topic<UUID, OrderEvent> topic, KafkaCluster kafka) {
		super(kafka, topic);
	}

}
