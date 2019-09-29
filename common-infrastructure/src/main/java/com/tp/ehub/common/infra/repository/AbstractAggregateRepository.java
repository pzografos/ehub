package com.tp.ehub.common.infra.repository;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.messaging.Event;
import com.tp.ehub.common.domain.messaging.function.AggregateReducer;
import com.tp.ehub.common.domain.messaging.store.PartitionedMessageStore;
import com.tp.ehub.common.domain.model.Aggregate;
import com.tp.ehub.common.domain.model.Entity;
import com.tp.ehub.common.domain.repository.AggregateRepository;
import com.tp.ehub.common.domain.repository.EntityCache;

/**
 * Base implementation of the <code>AggregateRepository</code>.
 *
 * <p>
 * It loads the aggregate by first looking for the aggregate root in the cache.
 * If not found it creates a new one. It then adds all the events found in the
 * store for the given key
 * </p>
 * <p>
 * Saving the aggregate means publishing all the events and then caching the
 * root entity at its latest state.
 * </p>
 * 
 * @param <K>
 *            The type of the aggregate's root entity unique identifier 
 * @param <E>
 *            The type of the <code>Event</code> that the aggregate is made of
 * @param <T>
 *            The type of the aggregate's root entity 
 * @param <A>
 *            The type of the <code>Aggregate</code> 
 */
public abstract class AbstractAggregateRepository<K, E extends Event<K>, T extends Entity, A extends Aggregate<K, E, T>> implements AggregateRepository<K, E, T, A> {

	@Inject
	protected EntityCache cache;

	@Inject
	protected PartitionedMessageStore store;
	
	@Inject
	protected AggregateReducer <K, E, T, A> aggregateEventReducer;
	
	@Inject
	Logger log;
	
	private Class<E> eventClass;
	
	private Class<T> rootEntityClass;
	
	protected AbstractAggregateRepository(Class<E> eventClass, Class<T> rootEntityClass) {
		this.eventClass = eventClass;
		this.rootEntityClass = rootEntityClass;
	}
	
	@Override
	public final A get(K key) {
		log.debug("Getting aggregate for key {}", key.toString());
		Optional<T> entityOpt = cache.get(key, rootEntityClass);
		if (entityOpt.isPresent()) {
			T rootEntity = entityOpt.get();
			return create(key, rootEntity);
		} else {
			T rootEntity = create(key);
			A aggregate = create(key, rootEntity);
			Collection<E> events = store.getbyKey(key, eventClass).collect(toList());
			aggregate = aggregateEventReducer.apply(aggregate, events);			
			return aggregate;
		}
	}

	@Override
	public final void save(A aggregate) {
		try {
			store.publish(aggregate.getNewEvents(), eventClass);
			cache.cache(aggregate.getKey(), aggregate.getRoot());
		} catch (Exception ex) {
			log.error(String.format("Failed to save %s. Reason: %s.", aggregate.getRoot().toString(), ex.getMessage()), ex);
			cache.evict(aggregate.getKey(), rootEntityClass);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Specifies a way to create the aggregate from a root entity
	 * 
	 * @param key
	 *            the aggregate key	 
	 * @param root
	 *            the root entity to start with
	 * @return the updated Aggregate
	 */
	protected abstract A create(K key, T root);

	/**
	 * Specifies a way to create a new entity for the aggregate
	 *
	 * @param key
	 *            the aggregate key
	 * @return the new entity instance
	 */
	protected abstract T create(K key);
	
}
