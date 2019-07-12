package com.tp.ehub.command.messaging;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.command.Command;
import com.tp.ehub.common.infra.messaging.kafka.container.AbstractTopic;

@ApplicationScoped
@Named("commands")
public class CommandsTopic extends AbstractTopic<String, Command> {

	@Inject
	public CommandsTopic(@Named("objectMapper") ObjectMapper objectMapper) {
		super("commands", objectMapper, String.class, Command.class);
	}
}
