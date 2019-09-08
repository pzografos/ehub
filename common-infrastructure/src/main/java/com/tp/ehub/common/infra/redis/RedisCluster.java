package com.tp.ehub.common.infra.redis;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.tp.ehub.common.infra.property.EhubProperty;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * Redis representation.
 *
 * <p>
 * Maintains the cluster hosts and a thread-safe <code>RedisClusterClient</code>
 * </p>
 */
@ApplicationScoped
public class RedisCluster {

	@Inject
	@EhubProperty("REDIS_NODES")
	String nodes;
		
	private RedisClient client;

	@PostConstruct
	public void init() {
		this.client = RedisClient.create(nodes);
	}
	
	/**
	 * Get the <code>RedisClusterClient</code>
	 * 
	 * @return the underlying <code>RedisClusterClient</code>
	 */
	public RedisClient getClient() {
		return this.client;
	}

	/**
	 * Get a new cluster connection
	 * 
	 * @return a new cluster connection
	 */
	public StatefulRedisConnection<String, String> getConnection() {
		return client.connect();
	}
}
