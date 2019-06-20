package com.tp.ehub.product.factory;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.command.model.messaging.kafka.CommandsTopic;
import com.tp.ehub.messaging.kafka.KafkaCluster;
import com.tp.ehub.messaging.kafka.service.ConsumerGroupKafkaReceiver;
import com.tp.ehub.messaging.kafka.service.Receiver;
import com.tp.ehub.order.model.OrderEventsTopic;
import com.tp.ehub.order.model.event.OrderEvent;
import com.tp.ehub.service.messaging.GlobalMessageReceiver;

@ApplicationScoped
public class ReceiverFactory {

	@Inject
	private KafkaCluster kafka;

	@Produces
	@ApplicationScoped
	@Receiver("commands-receiver")
	public GlobalMessageReceiver<String, Command> getCommands() {
		return new ConsumerGroupKafkaReceiver<String, Command>(kafka, CommandsTopic.get());
	}

	@Produces
	@ApplicationScoped
	@Receiver("order-events-receiver")
	public GlobalMessageReceiver<UUID, OrderEvent> getOrderEvents() {
		return new ConsumerGroupKafkaReceiver<UUID, OrderEvent>(kafka, OrderEventsTopic.get());
	}
	
}