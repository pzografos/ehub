package com.tp.ehub.command.model.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.command.Command;
import com.tp.ehub.factory.ObjectMapperFactory;
import com.tp.ehub.messaging.kafka.AbstractTopic;

public class CommandsTopic extends AbstractTopic<String, Command> {

	public static CommandsTopic get() {
		ObjectMapper mapper = ObjectMapperFactory.newInstance();
		mapper.registerModule(new CommandsModule());
		return new CommandsTopic(mapper);
	}
	
	private CommandsTopic(ObjectMapper mapper) {
		super("commands", mapper, String.class, Command.class);
	}
}
