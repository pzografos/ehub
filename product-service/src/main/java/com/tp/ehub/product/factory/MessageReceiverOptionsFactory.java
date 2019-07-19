package com.tp.ehub.product.factory;

import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class MessageReceiverOptionsFactory {

	public static final String CONSUMER_ID = "product_order_event_receiver_v1.0";

	@Produces
	public MessageReceiverOptions create() {
		return new MessageReceiverOptions(CONSUMER_ID, true);

	}
}
