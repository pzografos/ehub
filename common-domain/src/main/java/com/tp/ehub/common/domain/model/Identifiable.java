package com.tp.ehub.common.domain.model;

/**
 * Specifies that the implementing object should provide an identification value
 * which should be unique to the context that it is being used
 *
 * @param <K>
 *            the type of identification value
 */
public interface Identifiable<K> {

	/**
	 * Gets the unique identification value
	 * 
	 * @return the unique identification value
	 */
	K getId();
}
