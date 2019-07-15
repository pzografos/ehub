package com.tp.ehub.common.domain.repository;

import java.util.Optional;

import com.tp.ehub.common.domain.model.Entity;

public interface RootEntityCache<K, T extends Entity<K>> {

	Optional<T> get(K id);

	void cache(T entity);

	void evict(K id);
}
