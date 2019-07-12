package com.tp.ehub.common.domain.messaging.container;

import com.tp.ehub.common.domain.messaging.Message;

public interface MessageContainer<K, M extends Message<K>> {

	String getName();
}
