package com.tp.ehub.common.domain.messaging.receiver;

import java.util.Properties;

public interface MessageReceiverConfiguration {

	MessageReceiverOptions getOptions();

	Properties getProps();
}
