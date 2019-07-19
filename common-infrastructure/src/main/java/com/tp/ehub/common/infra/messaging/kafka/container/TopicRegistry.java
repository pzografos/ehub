package com.tp.ehub.common.infra.messaging.kafka.container;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.MessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;

@ApplicationScoped
public class TopicRegistry implements MessageContainerRegistry{

	@Inject @Any
	Instance<MessageContainer<?, ?>> topics;
	
	@SuppressWarnings("unchecked")
	@Override
	public <K, M extends Message<K>> MessageContainer<K, M> get(Class<M> type) {
		return topics.stream()
				.filter(t -> t.getMessageClass().equals(type))
				.map(topic -> (MessageContainer<K, M>) topic) 
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException(String.format("Could not find topic for class %s", type.getName())));
	}

}
