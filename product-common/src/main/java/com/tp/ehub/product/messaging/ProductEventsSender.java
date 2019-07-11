package com.tp.ehub.product.messaging;

import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Topic;
import com.tp.ehub.common.infra.messaging.kafka.sender.Sender;
import com.tp.ehub.common.infra.messaging.kafka.sender.TopicKafkaSender;
import com.tp.ehub.product.model.event.ProductEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

@ApplicationScoped
@Sender("product-events")
public class ProductEventsSender extends TopicKafkaSender<UUID, ProductEvent> {

	@Inject
	public ProductEventsSender(@Named("product-events") Topic<UUID, ProductEvent> topic, KafkaCluster kafkaCluster) {
		super(kafkaCluster, topic);
	}
}
