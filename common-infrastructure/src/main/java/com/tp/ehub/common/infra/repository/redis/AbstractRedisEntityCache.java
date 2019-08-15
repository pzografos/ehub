package com.tp.ehub.common.infra.repository.redis;


import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tp.ehub.common.domain.model.Entity;
import com.tp.ehub.common.domain.repository.EntityCache;

import io.lettuce.core.api.sync.RedisCommands;

public abstract class AbstractRedisEntityCache<K, T extends Entity> implements EntityCache<K, T> {

	@Inject
	RedisCluster redisCluster;
	
	private final JavaPropsMapper javaPropsMapper;
	
	private final JavaPropsSchema javaPropsSchema;
	
	private Class<T> entityClass;
	
	protected AbstractRedisEntityCache(Class<T> entityClass) {
		
		this.entityClass = entityClass;
		
		this.javaPropsMapper = new JavaPropsMapper();
		this.javaPropsMapper
			.registerModules(new JavaTimeModule())
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
			.getFactory().disable(Feature.CANONICALIZE_FIELD_NAMES);
		
		this.javaPropsSchema = JavaPropsSchema.emptySchema()
				.withWriteIndexUsingMarkers(true)
				.withParseSimpleIndexes(false)
				.withPathSeparator("->");
	}
	

	@Override
	public final Optional<T> get(K key) {
		
		requireNonNull(key);

		String storeRedisKey = storeKey().apply(key);

		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<String, String> hashValue = sync.hgetall(storeRedisKey);
		if (hashValue.isEmpty()) {
			return empty();
		}

		Properties properties = new Properties();
		properties.putAll(hashValue);

		return of(readProperties(properties));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	protected Function<K, String> storeKey(){
		return k -> k.toString();
	}
		
	protected Map<Object, Object> writeValueAsProperties(T element){
		try {
			return this.javaPropsMapper.writeValueAsProperties(element, javaPropsSchema);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected T readProperties(Properties properties) {
		try {
			return this.javaPropsMapper.readPropertiesAs(properties, javaPropsSchema, this.entityClass);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
