package com.tp.ehub.common.domain.repository;

import java.util.Optional;

import com.tp.ehub.common.domain.model.Entity;

public interface EntityCache {

	<K, T extends Entity> Optional<T> get(K key,  Class<T> entityClass);

	<K, T extends Entity> void cache(K key, T entity);

	<K, T extends Entity> void evict(K key,  Class<T> entityClass);
}
