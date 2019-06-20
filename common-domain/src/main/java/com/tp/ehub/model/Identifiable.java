package com.tp.ehub.model;

/**
 * Specifies that the implementing object should provide an Identification value
 * which should be unique to the context that it is being used
 *
 * @param <K> the type of identification value
 */
public interface Identifiable<K> {

	/**
	 * Gets the unique identification value
	 * 
	 * @return the unique identification value
	 */
	K getId();
}
