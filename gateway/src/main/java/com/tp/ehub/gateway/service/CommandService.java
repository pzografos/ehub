package com.tp.ehub.gateway.service;

import static reactor.core.publisher.Flux.just;

import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.messaging.kafka.KafkaRecord;
import com.tp.ehub.service.messaging.MessageSender;

public class CommandService {

	@Inject
	private MessageSender<String, Command> sender;
	
	public void create(Command command) throws Exception {
		sender.send(just(new KafkaRecord<String, Command>(command.getKey(), command)));
	}

}
