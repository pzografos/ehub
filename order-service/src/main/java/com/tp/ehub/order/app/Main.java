package com.tp.ehub.order.app;

import java.util.UUID;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import com.tp.ehub.command.Command;
import com.tp.ehub.service.messaging.GlobalMessageReceiver;

public class Main {

	public static void main(String[] args) {

		SeContainer container = SeContainerInitializer.newInstance().initialize();
		GlobalMessageReceiver<UUID, Command> commandsReceiver = (GlobalMessageReceiver<UUID, Command>) container.getBeanManager().getBeans("commands-receiver");

	}
}
