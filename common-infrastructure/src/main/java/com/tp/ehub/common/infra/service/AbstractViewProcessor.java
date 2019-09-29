package com.tp.ehub.common.infra.service;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.messaging.function.ViewReducer;
import com.tp.ehub.common.domain.model.View;
import com.tp.ehub.common.domain.repository.ViewRepository;
import com.tp.ehub.common.infra.messaging.kafka.AbstractMessageProcessor;

public abstract class AbstractViewProcessor<K1, E extends Event<K1>, K2, V extends View<K2>> extends AbstractMessageProcessor<K1, E>{

	@Inject
	ViewRepository viewRepository;
	
	@Inject
	ViewReducer<K1, E, K2, V> viewReducer;
	
	Class<V> viewClass;
	
	protected AbstractViewProcessor(String consumerId, Class<E> messageClass, Class<V> viewClass) {
		super(consumerId, messageClass);
		this.viewClass = viewClass;
	}

	@Override
	public void accept(E event) {
		K2 key = getViewKey(event);
		V view = viewRepository.get(key, viewClass).orElse(createView(key));
		view = viewReducer.apply(view, event);
		viewRepository.save(view, viewClass);		
	}
		
	protected abstract K2 getViewKey(E event);
	
	protected abstract V createView(K2 key);
}
