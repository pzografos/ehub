package com.tp.ehub.repository;

import java.util.Optional;

import com.tp.ehub.model.entity.Entity;

public interface EntityCache<K, T extends Entity<K>> {

	Optional<T> get(K id);

	void cache(T entity);

	void evict(K id);
}
