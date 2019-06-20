package com.tp.ehub.model.event;

import com.tp.ehub.model.Versionable;
import com.tp.ehub.model.messaging.Message;

/**
 * The <code>Event</code> represents something that has happened in the domain.
 * 
 * <p>
 * Events have keys that are not unique per event but help the application to
 * group the events in a meaningful way.
 * </p> 
 * 
 * @param <K> the type of key to user for the event
 */
public interface Event<K> extends Message, Versionable {

	/**
	 * Gets the event key
	 * 
	 * @return the event key
	 */
	public K getKey();

	/**
	 * Gets a name for this event that uniquely identifies it among other events in
	 * the system
	 * 
	 * @return the event name
	 */
	public String getEventName();
}
