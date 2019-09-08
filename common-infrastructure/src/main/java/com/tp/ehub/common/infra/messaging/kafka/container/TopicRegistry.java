package com.tp.ehub.common.infra.messaging.kafka.container;

import static java.lang.String.format;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.common.domain.messaging.Message;
import com.tp.ehub.common.domain.messaging.container.Container;
import com.tp.ehub.common.domain.messaging.container.MessageContainer;
import com.tp.ehub.common.domain.messaging.container.MessageContainerRegistry;

@ApplicationScoped
public class TopicRegistry implements MessageContainerRegistry{
	
	@Inject
	@Named("objectMapper")
	protected ObjectMapper mapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public <K, M extends Message<K>> MessageContainer<K, M> get(Class<M> type) {
		
		Class<K> keyClass; 
		
		if (type.isAnnotationPresent(Container.class)) {
			Annotation annotation = type.getAnnotation(Container.class);
			Container container = (Container) annotation;
			keyClass = (Class<K>) container.keyClass();
			return new Topic<K,M>(container.name(), keyClass, type, mapper);
		} else {
			throw new IllegalArgumentException(format("Class %s is not annotated with a 'Container' annotation. Cannot resolve topic.", type));
		}
	}

}
