package com.tp.ehub.common.infra.repository.redis;


import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import javax.inject.Inject;

import com.tp.ehub.common.domain.model.Entity;
import com.tp.ehub.common.domain.repository.EntityCache;

import io.lettuce.core.api.sync.RedisCommands;

public abstract class AbstractRedisEntityCache<K, T extends Entity> implements EntityCache<K, T> {

	@Inject
	RedisCluster redisCluster;

	@Override
	public final Optional<T> get(K key) {
		
		requireNonNull(key);

		String storeRedisKey = storeKey().apply(key);

		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<String, String> hashValue = sync.hgetall(storeRedisKey);
		if (hashValue.isEmpty()) {
			return Optional.empty();
		}

		Properties properties = new Properties();
		properties.putAll(hashValue);

		return of(readProperties(properties));
	}

	@Override
	public final void cache(K key, T entity) {
		
		requireNonNull(key);
		requireNonNull(entity);
		
		String storeRedisKey = storeKey().apply(key);
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<Object, Object> hashValue = writeValueAsProperties(entity);
		sync.hmset(storeRedisKey, (Map) hashValue);
	}

	@Override
	public final void evict(K key) {
		
		requireNonNull(key);

		String storeRedisKey = storeKey().apply(key);

		RedisCommands<String, String> sync = redisCluster.getConnection().sync();

		sync.del(storeRedisKey);
	}

	protected abstract Function<K, String> storeKey();

	protected abstract Map<Object, Object> writeValueAsProperties(T element);

	protected abstract T readProperties(Properties properties);

}
