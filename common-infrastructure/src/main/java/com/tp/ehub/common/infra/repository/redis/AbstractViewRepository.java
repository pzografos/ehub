package com.tp.ehub.common.infra.repository.redis;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tp.ehub.common.domain.model.View;
import com.tp.ehub.common.domain.repository.ViewRepository;

import io.lettuce.core.api.sync.RedisCommands;

public abstract class AbstractViewRepository<K, V extends View<K>> implements ViewRepository<K, V>{

	@Inject
	RedisCluster redisCluster;
	
	private final JavaPropsMapper javaPropsMapper;
	
	private final JavaPropsSchema javaPropsSchema;
	
	private Class<V> viewClass;
	
	protected AbstractViewRepository(Class<V> viewClass) {
		
		this.viewClass = viewClass;
		
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
	public V get(K key) {
		
		requireNonNull(key);

		String storeRedisKey = storeKey().apply(key);

		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<String, String> hashValue = sync.hgetall(storeRedisKey);
		if (hashValue.isEmpty()) {
			return getNewView(key);
		}

		Properties properties = new Properties();
		properties.putAll(hashValue);

		return readProperties(properties);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void save(V view) {

		requireNonNull(view);
		
		String storeRedisKey = storeKey().apply(view.getKey());
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<Object, Object> hashValue = writeValueAsProperties(view);
		sync.hmset(storeRedisKey, (Map) hashValue);
		
	}

	protected abstract V getNewView(K key);
	
	protected Function<K, String> storeKey(){
		return k -> k.toString();
	}
		
	protected Map<Object, Object> writeValueAsProperties(V element){
		try {
			return this.javaPropsMapper.writeValueAsProperties(element, javaPropsSchema);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected V readProperties(Properties properties) {
		try {
			return this.javaPropsMapper.readPropertiesAs(properties, javaPropsSchema, this.viewClass);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
