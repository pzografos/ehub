package com.tp.ehub.gateway.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.command.model.messaging.kafka.CommandsTopic;
import com.tp.ehub.messaging.kafka.KafkaCluster;
import com.tp.ehub.messaging.kafka.service.TopicKafkaSender;
import com.tp.ehub.service.messaging.MessageSender;

@ApplicationScoped
public class MessagingFactory {

	@Inject
	private KafkaCluster kafka;

	@Produces
	@ApplicationScoped
	public MessageSender<String, Command> getCommandProducer() {
		return new TopicKafkaSender<String, Command>(kafka, CommandsTopic.get());
	}

}
