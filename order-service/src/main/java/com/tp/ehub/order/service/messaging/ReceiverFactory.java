package com.tp.ehub.order.service.messaging;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.tp.ehub.command.Command;
import com.tp.ehub.command.model.messaging.kafka.CommandsTopic;
import com.tp.ehub.messaging.kafka.KafkaCluster;
import com.tp.ehub.messaging.kafka.service.ConsumerGroupKafkaReceiver;
import com.tp.ehub.service.messaging.GlobalMessageReceiver;

@ApplicationScoped
public class ReceiverFactory {

	@Inject
	private KafkaCluster kafka;

	@Produces
	@ApplicationScoped
	@Named("commands-receiver")
	public GlobalMessageReceiver<String, Command> commands() {
		return new ConsumerGroupKafkaReceiver<String, Command>(kafka, CommandsTopic.get());
	}

}