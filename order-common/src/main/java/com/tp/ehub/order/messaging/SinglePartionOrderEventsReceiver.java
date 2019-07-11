package com.tp.ehub.order.messaging;

import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Topic;
import com.tp.ehub.common.infra.messaging.kafka.receiver.Receiver;
import com.tp.ehub.common.infra.messaging.kafka.receiver.SinglePartitionKafkaReceiver;
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
