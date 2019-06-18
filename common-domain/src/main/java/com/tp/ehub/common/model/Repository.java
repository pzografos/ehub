package com.tp.ehub.common.model;

import java.util.Optional;

/**
 * The <code>Repository</code> provides endpoints to save and retrieve domain
 * objects to and from a data store
 *
 * @param <K> The type of id to use for the domain object identification
 * @param <T> The type of domain object
 */
public interface Repository<K, T> {

	Optional<T> get(K id);
	
	void save(T object);
}
