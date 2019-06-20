package com.tp.ehub.repository;

import java.util.Optional;

import com.tp.ehub.model.entity.Entity;

public interface EntityCache<K, T extends Entity<K>> {

	public Optional<T> get(K id);
	
	public void cache(T entity);
	
	public void evict(K id);
}

