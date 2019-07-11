package com.tp.ehub.product.messaging;

import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Topic;
import com.tp.ehub.common.infra.messaging.kafka.receiver.ConsumerGroupKafkaReceiver;
import com.tp.ehub.common.infra.messaging.kafka.receiver.Receiver;
import com.tp.ehub.product.model.event.ProductEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

@ApplicationScoped
@Receiver("product-events")
public class ConsumerGroupProductEventsReceiver extends ConsumerGroupKafkaReceiver<UUID, ProductEvent> {

	@Inject
	public ConsumerGroupProductEventsReceiver(@Named("product-events") Topic<UUID, ProductEvent> topic, KafkaCluster kafka) {
		super(kafka, topic);
	}
}
