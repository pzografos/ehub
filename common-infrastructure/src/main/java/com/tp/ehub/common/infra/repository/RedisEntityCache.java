package com.tp.ehub.common.infra.repository;


import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tp.ehub.common.domain.model.Entity;
import com.tp.ehub.common.domain.repository.EntityCache;
import com.tp.ehub.common.infra.redis.RedisCluster;

import io.lettuce.core.api.sync.RedisCommands;

@Alternative
@ApplicationScoped
public class RedisEntityCache implements EntityCache {

	@Inject
	RedisCluster redisCluster;
	
	private final JavaPropsMapper javaPropsMapper;
	
	private final JavaPropsSchema javaPropsSchema;
		
	public RedisEntityCache() {
				
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
	public <K, T extends Entity> Optional<T> get(K key, Class<T> entityClass) {
		requireNonNull(key);

		String storeRedisKey = key.toString();

		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<String, String> hashValue = sync.hgetall(storeRedisKey);
		if (hashValue.isEmpty()) {
			return empty();
		}

		Properties properties = new Properties();
		properties.putAll(hashValue);

		return of(readProperties(properties, entityClass));
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <K, T extends Entity> void cache(K key, T entity) {
		
		requireNonNull(key);
		requireNonNull(entity);
		
		String storeRedisKey = key.toString();
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<Object, Object> hashValue = writeValueAsProperties(entity);
		sync.hmset(storeRedisKey, (Map) hashValue);		
	}

	@Override
	public <K, T extends Entity> void evict(K key, Class<T> entityClass) {
		
		requireNonNull(key);

		String storeRedisKey = key.toString();

		RedisCommands<String, String> sync = redisCluster.getConnection().sync();

		sync.del(storeRedisKey);		
	}
	
	<T> Map<Object, Object> writeValueAsProperties(T element){
		try {
			return this.javaPropsMapper.writeValueAsProperties(element, javaPropsSchema);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	<T> T readProperties(Properties properties, Class<T> entityClass) {
		try {
			return this.javaPropsMapper.readPropertiesAs(properties, javaPropsSchema, entityClass);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
