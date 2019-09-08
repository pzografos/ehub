package com.tp.ehub.common.infra.repository;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tp.ehub.common.domain.model.View;
import com.tp.ehub.common.domain.repository.ViewRepository;
import com.tp.ehub.common.infra.redis.RedisCluster;

import io.lettuce.core.api.sync.RedisCommands;

@ApplicationScoped
public class RedisViewRepository implements ViewRepository{

	@Inject
	RedisCluster redisCluster;
	
	private final JavaPropsMapper javaPropsMapper;
	
	private final JavaPropsSchema javaPropsSchema;
	
	public RedisViewRepository() {
				
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
	public <K, V extends View<K>> Optional<V> get(K key, Class<V> viewClass) {
		requireNonNull(key);

		String storeRedisKey = storeKey(viewClass).apply(key);

		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<String, String> hashValue = sync.hgetall(storeRedisKey);
		if (hashValue.isEmpty()) {
			return Optional.empty();
		} else {
			
			Properties properties = new Properties();
			properties.putAll(hashValue);

			return Optional.of(readProperties(properties, viewClass));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <K, V extends View<K>> void save(V view, Class<V> viewClass) {
		
		requireNonNull(view);
		
		String storeRedisKey = storeKey(viewClass).apply(view.getKey());
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		Map<Object, Object> hashValue = writeValueAsProperties(view);
		sync.hmset(storeRedisKey, (Map) hashValue);		
	}

	
	<K, V extends View<K>> Function<K, String> storeKey(Class<V> viewClass){
		return k -> format("view_%s_%s", viewClass.getSimpleName(), k.toString());
	}
		
	<K, V extends View<K>> Map<Object, Object> writeValueAsProperties(V element){
		try {
			return this.javaPropsMapper.writeValueAsProperties(element, javaPropsSchema);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	<K, V extends View<K>> V readProperties(Properties properties, Class<V> viewClass) {
		try {
			return this.javaPropsMapper.readPropertiesAs(properties, javaPropsSchema, viewClass);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
