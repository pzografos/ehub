package com.tp.ehub.gateway.service;

import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.messaging.kafka.KafkaRecord;
import com.tp.ehub.messaging.kafka.service.Sender;
import com.tp.ehub.service.messaging.MessageSender;

import reactor.core.publisher.Flux;

public class CommandService {

	@Inject
	@Sender("commands")
	MessageSender<String, Command> commandsTopicSender;

	public void create(Command command) {
		commandsTopicSender.send(Flux.just(new KafkaRecord<String, Command>(command.getKey(), command)));
	}

}
