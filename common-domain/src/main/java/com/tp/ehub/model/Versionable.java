package com.tp.ehub.model;

/**
 * Specifies that the implementing class should have a version property allowing
 * to identify the last change that was applied to it.
 *
 */
public interface Versionable {

	/**
	 * Gets the version value
	 * 
	 * @return the version
	 */
	Long getVersion();
}
