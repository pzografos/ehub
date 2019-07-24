package com.tp.ehub.common.domain.messaging;

import com.tp.ehub.common.domain.model.Versionable;

/**
 * The <code>Event</code> represents something that has happened in the domain.
 * 
 * <p>
 * Events have keys that are not unique per event but help the application to
 * group the events in a meaningful way.
 * </p>
 * 
 * @param <K>
 *            the type of key to user for the event
 */
public interface Event<K> extends Message<K>, Versionable {

	String getEventName();
}
