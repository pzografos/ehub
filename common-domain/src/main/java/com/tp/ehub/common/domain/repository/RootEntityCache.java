package com.tp.ehub.common.domain.repository;

import java.util.Optional;

import com.tp.ehub.common.domain.model.RootEntity;

public interface RootEntityCache<K, T extends RootEntity<K>> {

	Optional<T> get(K id);

	void cache(T entity);

	void evict(K id);
}
