package com.tp.ehub.common.infra.repository;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.tp.ehub.common.domain.model.Entity;
import com.tp.ehub.common.domain.repository.EntityCache;

@ApplicationScoped
public class EmptyEntityCache implements EntityCache {

	@Inject
	Logger log;
	
	@Override
	public <K, T extends Entity> Optional<T> get(K key, Class<T> entityClass) {
		log.debug("Retrieving entity {} for key {}", key.toString(), entityClass.getCanonicalName());
		return Optional.empty();
	}

	@Override
	public <K, T extends Entity> void cache(K key, T entity) {
		log.debug("Caching entity {} for key {}", key.toString(), entity.getClass().getCanonicalName());
		
	}

	@Override
	public <K, T extends Entity> void evict(K key, Class<T> entityClass) {
		log.debug("Evicting entity {} for key {}", key.toString(), entityClass.getCanonicalName());
	}

}
