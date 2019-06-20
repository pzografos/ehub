package com.tp.ehub.product.factory;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.tp.ehub.messaging.kafka.KafkaCluster;
import com.tp.ehub.messaging.kafka.service.SinglePartitionKafkaReceiver;
import com.tp.ehub.messaging.kafka.service.TopicKafkaSender;
import com.tp.ehub.product.model.event.ProductEvent;
import com.tp.ehub.product.model.event.ProductEventsTopic;
import com.tp.ehub.service.messaging.KeyMessageReceiver;
import com.tp.ehub.service.messaging.MessageSender;

@ApplicationScoped
public class MessagingFactory {

	@Inject
	private KafkaCluster kafka;
	
	@Produces
	@ApplicationScoped
	private KeyMessageReceiver<UUID, ProductEvent> getProductEventsKeyReceiver(){
		return new SinglePartitionKafkaReceiver<UUID, ProductEvent>(kafka, ProductEventsTopic.get());
	}

	@Produces
	@ApplicationScoped
	private MessageSender<UUID, ProductEvent> getProductEventsSender(){
		return new TopicKafkaSender<UUID, ProductEvent>(kafka, ProductEventsTopic.get());
	}
	
}
