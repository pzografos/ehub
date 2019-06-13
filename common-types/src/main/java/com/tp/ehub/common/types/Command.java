package com.tp.ehub.common.types;

import java.time.OffsetDateTime;

/**
 * The <code>Command</code> represents a system trigger which aims at creating a
 * change in the application's state.
 */
public interface Command {

	/**
	 * Get the timestamp of the message
	 * 
	 * @return the timestamp of the message
	 */
	OffsetDateTime timestamp();
}