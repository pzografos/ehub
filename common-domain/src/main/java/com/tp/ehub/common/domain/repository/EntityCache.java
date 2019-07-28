package com.tp.ehub.common.domain.repository;

import java.util.Optional;

import com.tp.ehub.common.domain.model.Entity;

public interface EntityCache<K, T extends Entity> {

	Optional<T> get(K key);

	void cache(K key, T entity);

	void evict(K key);
}
