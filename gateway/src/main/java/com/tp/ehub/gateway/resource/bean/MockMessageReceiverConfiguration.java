package com.tp.ehub.gateway.resource.bean;

import com.tp.ehub.common.domain.messaging.receiver.MessageReceiver;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverConfiguration;
import com.tp.ehub.common.domain.messaging.receiver.MessageReceiverOptions;
import com.tp.ehub.common.infra.qualifier.Standard;

import java.util.Properties;

@Standard
public class MockMessageReceiverConfiguration implements MessageReceiverConfiguration {

	@Override
	public MessageReceiverOptions getOptions() {
		return null;
	}

	@Override
	public Properties getProps() {
		return null;
	}
}
