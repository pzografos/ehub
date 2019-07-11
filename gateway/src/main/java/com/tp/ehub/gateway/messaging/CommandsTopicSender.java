package com.tp.ehub.gateway.messaging;

import com.tp.ehub.command.Command;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Topic;
import com.tp.ehub.common.infra.messaging.kafka.sender.Sender;
import com.tp.ehub.common.infra.messaging.kafka.sender.TopicKafkaSender;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
@Sender("commands")
public class CommandsTopicSender extends TopicKafkaSender<String, Command> {

	@Inject
	public CommandsTopicSender(@Named("commands") Topic<String, Command> topic, KafkaCluster kafka) {
		super(kafka, topic);
	}
}
