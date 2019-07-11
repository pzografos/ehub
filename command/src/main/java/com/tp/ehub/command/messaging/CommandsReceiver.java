package com.tp.ehub.command.messaging;

import com.tp.ehub.command.Command;
import com.tp.ehub.common.infra.messaging.kafka.KafkaCluster;
import com.tp.ehub.common.infra.messaging.kafka.Topic;
import com.tp.ehub.common.infra.messaging.kafka.receiver.ConsumerGroupKafkaReceiver;
import com.tp.ehub.common.infra.messaging.kafka.receiver.Receiver;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
@Receiver("commands")
public class CommandsReceiver extends ConsumerGroupKafkaReceiver<String, Command> {

	@Inject
	public CommandsReceiver(KafkaCluster kafka, @Named("commands") Topic<String, Command> topic) {
		super(kafka, topic);
	}
}
