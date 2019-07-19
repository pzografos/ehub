package com.tp.ehub.common.domain.model;

/**
 * Specifies that the implementing class should have a version qualifier
 * allowing to identify the last change that was applied to it.
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
