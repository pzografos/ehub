package com.tp.ehub.common.model;

import java.time.OffsetDateTime;
import java.util.UUID;

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
	
	/**
	 * Get the company UUID of the message
	 * 
	 * @return the company UUID of the message
	 */
	UUID company();
}