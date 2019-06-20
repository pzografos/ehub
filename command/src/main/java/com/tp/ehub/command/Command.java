package com.tp.ehub.command;

import com.tp.ehub.model.messaging.Message;
import com.tp.ehub.serialization.JsonMessage;

@JsonMessage
public interface Command extends Message {
	
	String getKey();

	String getCommandName();

}
