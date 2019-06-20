package com.tp.ehub.repository.aggregate;

import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.ehub.model.aggregate.Aggregate;
import com.tp.ehub.model.entity.Entity;
import com.tp.ehub.model.event.Event;
import com.tp.ehub.repository.AggregateRepository;
import com.tp.ehub.repository.EntityCache;
import com.tp.ehub.repository.MessageStore;

/**
 * Base implementation of the <code>AggregateRepository</code>.
 * 
 * <p>
 * It loads the aggregate by first looking for the aggregate root in the cache.
 * If not found it creates a new one. It then adds all the events from the point
 * of the entity to the latest found in the store. If no starting point is given
 * (case for new entity) the aggregate loads all the events
 * </p>
 * <p>
 * Saving the aggregate means publishing all the events and then caching the
 * root entity at its latest state. The caching step is effectively optional as
 * if the entity is loading it from a previous point will make no difference in
 * functionality only in performance.
 * </p>
 * 
 * @param <A> The type of the <code>Aggregate</code>
 * @param <E> The type of the <code>Event</code> that the aggregate is made of
 * @param <T> The type of the aggregate's root entity
 * @param <K> The type of the aggregate's root entity unique identifier
 */
public abstract class AbstractAggregateRepository<A extends Aggregate<E, T, K>, E extends Event<K>, T extends Entity<K>, K>
		implements AggregateRepository<A, E, T, K> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAggregateRepository.class);

	@Inject
	private EntityCache<K, T> cache;

	@Inject
	private MessageStore<K, E> store;

	protected AbstractAggregateRepository() {

	}

	@Override
	public final A get(K id) {
		Optional<T> entityOpt = cache.get(id);
		if (entityOpt.isPresent()) {
			T rootEntity = entityOpt.get();
			return create(rootEntity);
		} else {
			T rootEntity = create(id);
			A aggregate = create(rootEntity);
			Stream<E> events = store.getbyKey(id);
			events.forEach(aggregate::apply);
			return aggregate;
		}
	}

	@Override
	public final void save(A aggregate) {
		try {
			store.publish(aggregate.getNewEvents().map(EventMessageRecord::new));
			cache.cache(aggregate.getRoot());
		} catch (Exception ex) {
			LOGGER.error(String.format("Failed to save %s. Reason: %s.", aggregate.getRoot().toString(), ex.getMessage()), ex);
			cache.evict(aggregate.getRoot().getId());
		}
	}

	/**
	 * Specifies a way to create the aggregate from a root entity
	 * 
	 * @param root the root entity to start with
	 * @return the updated Aggregate
	 */
	protected abstract A create(T root);

	/**
	 * Specifies a way to create a new entity for the aggregate
	 * 
	 * @param id the new enitity's ID
	 * @return the new entity instance
	 */
	protected abstract T create(K id);
	
}
