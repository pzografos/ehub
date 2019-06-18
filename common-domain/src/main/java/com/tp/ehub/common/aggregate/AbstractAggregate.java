package com.tp.ehub.common.aggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import javax.inject.Inject;

import com.tp.ehub.common.event.Event;
import com.tp.ehub.common.event.EventStore;
import com.tp.ehub.common.model.Entity;
import com.tp.ehub.common.model.Repository;

/**
 * Base implementation for the <code>AggregateEntity</code>.
 *
 * @param <E> The type of the <code>Event</code> that the aggregate is made of
 * @param <T> The type of the aggregate's root entity
 * @param <K> The type of the aggregate's root entity unique identifier
 */
public abstract class AbstractAggregate<E extends Event, T extends Entity<K>, K>
		implements Aggregate<E, T, K> {

	@Inject
	private Repository<K, T> repository;

	@Inject
	private EventStore<E> store;

	private T rootEntity;
	
	private Collection<E> newEvents = new ArrayList<>();

	@Override
	public T root() {
		return this.rootEntity;
	}
	
	@Override
	public final void load(K id) {

		this.rootEntity = repository.get(id).orElse(createRoot(id));

		store.events(eventsFilter()).forEach(e -> rootEntity = mutate(rootEntity, e));
	}

	@Override
	public final void apply(E event){
		Objects.requireNonNull(rootEntity, "Apply was called before load");
		if (validate(event)) {
			rootEntity = mutate(rootEntity, event);
			newEvents.add(event);
		} else {
			throw new IllegalArgumentException(String.format("Could not apply event [%s]. Have you validated before applying?"));
		}
	}

	@Override
	public final void publish() {
		Objects.requireNonNull(rootEntity, "Save was called before load");
		store.publish(newEvents);
		repository.save(rootEntity);
		newEvents.clear();
	}

	/**
	 * Applies an event to the root entity
	 * 
	 * @param entity the previous state of the root entity
	 * @param event the event to apply
	 * 
	 * @return the updated entity
	 */
	protected abstract T mutate(T entity, E event);
	
	/**
	 * Creates a new root entity if none is found in the repository.
	 * 
	 * @return the newly created entity
	 */
	protected abstract T createRoot(K id);
	
	/**
	 * Validates an Event before it is applied to the aggregate. Please note that
	 * this is by no means a validation you should rely on. The application should
	 * have validated the event before sending it to the aggregate. You can use the
	 * root() function to get the root entity and perform any validation you want
	 * based on it.
	 * 
	 * @param event the event to validate
	 * @return true if the event can be applied to the aggregate. 
	 */
	protected boolean validate(E event) {
		return true;
	}

	/**
	 * Defines a filter to be applied to the events coming from the event store to
	 * eliminate those that do not affect the aggregate
	 * 
	 * @return the predicate to filter with
	 */
	protected Predicate<E> eventsFilter() {
		return $ -> true;
	};

}
