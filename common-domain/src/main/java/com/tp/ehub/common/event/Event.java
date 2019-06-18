package com.tp.ehub.common.event;

import java.time.OffsetDateTime;

/**
 * The <code>Event</code> is the source of truth for e-hub, representing the
 * changes in the application's state.
 */
public interface Event {

	/**
	 * Get the timestamp of the message
	 * 
	 * @return the timestamp of the message
	 */
	OffsetDateTime timestamp();
}
