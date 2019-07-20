package com.tp.ehub.order.app;

import java.util.function.Consumer;

import javax.inject.Inject;

import com.tp.ehub.command.Command;
import com.tp.ehub.common.domain.messaging.MessageRecord;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

public class CommandHandler implements Consumer<Command> {

	@Inject
	MessageReceiver commandsReceiver;

	@Inject
	OrderCommandConsumerVisitor commandConsumer;

	public void run(Scheduler productScheduler) {
		MessageReceiverOptions options = new MessageReceiverOptions();
		options.setConsumerId("order_command_receiver_v1.0");
		options.setFromStart(true);
		final Flux<Command> commandsFlux = commandsReceiver.receiveAll(Command.class, options) 
				.map(MessageRecord::getMessage)
				.subscribeOn(productScheduler);
		commandsFlux.subscribe(this);
	}

	@Override
	public void accept(Command command) {
		commandConsumer.accept(command);
	}

}
