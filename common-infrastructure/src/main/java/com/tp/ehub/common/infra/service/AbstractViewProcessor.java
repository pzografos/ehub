package com.tp.ehub.common.infra.service;

import javax.inject.Inject;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.messaging.function.ViewReducer;
import com.tp.ehub.common.domain.model.View;
import com.tp.ehub.common.domain.repository.ViewRepository;

public abstract class AbstractViewProcessor<K1, E extends Event<K1>, K2, V extends View<K2>> extends AbstractMessageProcessor<K1, E>{

	@Inject
	ViewRepository<K2, V> viewRepository;
	
	@Inject
	ViewReducer<K1, E, K2, V> viewReducer;
	
	protected AbstractViewProcessor(String consumerId, Class<E> messageClass) {
		super(consumerId, messageClass);
	}

	@Override
	public void accept(E event) {
		V view = viewRepository.get(getViewKey(event));
		view = viewReducer.apply(view, event);
		viewRepository.save(view);		
	}
		
	protected abstract K2 getViewKey(E event);
}
