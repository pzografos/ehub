package com.tp.ehub.gateway.service;

import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.common.domain.messaging.sender.MessageSender;
import com.tp.ehub.common.infra.messaging.kafka.KafkaRecord;

import reactor.core.publisher.Flux;

public class CommandService {

	@Inject
	MessageSender sender;

	public void create(Command command) {
		sender.send(Flux.just(new KafkaRecord<String, Command>(command.getKey(), command)), Command.class);
	}

}
