package com.tp.ehub.repository;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import javax.inject.Inject;

import com.tp.ehub.database.redis.RedisCluster;
import com.tp.ehub.model.entity.Entity;

import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

public abstract class AbstractRedisEntityCache<K, T extends Entity<K>> implements EntityCache<K, T> {

	@Inject
	private RedisCluster redisCluster;

	@Override
	public final Optional<T> get(K id) {

		String storeRedisKey = storeKey().apply(id);

		RedisAdvancedClusterCommands<String, String> sync = redisCluster.clusterConnection().sync();

		Map<String, String> hashValue = sync.hgetall(storeRedisKey);
		if (hashValue.isEmpty()) {
			return Optional.empty();
		}

		Properties properties = new Properties();
		properties.putAll(hashValue);

		return Optional.of(readProperties(properties));
	}

	@Override
	public final void cache(T entity) {
		String storeRedisKey = storeKey().apply(entity.getId());
		try {
			this.redisCluster.nodeCommandSyncExecutor().call(storeRedisKey, nc -> this.put(entity.getId(), entity, nc));
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final void evict(K id) {
		// TODO implement
		throw new RuntimeException("Not implemented yet");
	}

	@SuppressWarnings({ "unchecked" })
	private EntityCache<K, T> put(K key, T updatedElement, StatefulRedisConnection<String, String> connection) {
		String storeRedisKey = storeKey().apply(key);

		RedisCommands<String, String> sync = connection.sync();

		boolean exists = sync.exists(storeRedisKey) != 0L;

		if (!exists && updatedElement == null) {
			// no-op delete for migrated old state values (e.g. multiple
			// tombstones)
			return this;
		}

		sync.multi();
		if (updatedElement != null) {
			Map<Object, Object> hashValue = writeValueAsProperties(updatedElement);
			sync.del(storeRedisKey);
			sync.hmset(storeRedisKey, (Map) hashValue);
		} else {
			sync.del(storeRedisKey);
		}
		TransactionResult result = sync.exec();

		Exception exception = result.stream()
				.filter(commandResult -> Exception.class.isAssignableFrom(commandResult.getClass()))
				.map(Exception.class::cast)
				.findFirst()
				.orElse(null);

		if (exception != null) {
			throw new RuntimeException(exception);
		}

		return this;
	}

	protected abstract Function<K, String> storeKey();

	protected abstract Map<Object, Object> writeValueAsProperties(T element);

	protected abstract T readProperties(Properties properties);

}
