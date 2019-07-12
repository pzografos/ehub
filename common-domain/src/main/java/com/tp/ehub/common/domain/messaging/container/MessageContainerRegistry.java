package com.tp.ehub.common.domain.messaging.container;

import com.tp.ehub.common.domain.messaging.Message;

public interface MessageContainerRegistry {

	public <K, M extends Message<K>> MessageContainer<K,M> get(Class<M> type);
}
