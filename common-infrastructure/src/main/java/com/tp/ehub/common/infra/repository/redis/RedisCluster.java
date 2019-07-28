package com.tp.ehub.common.infra.repository.redis;

import java.util.Objects;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * Redis representation.
 *
 * <p>
 * Maintains the cluster hosts and a thread-safe <code>RedisClusterClient</code>
 * </p>
 */
public class RedisCluster {

	private final RedisClient client;

	private RedisCluster(RedisClient client) {
		this.client = client;
	}

	/**
	 * Get the <code>RedisClusterClient</code>
	 * 
	 * @return the underlying <code>RedisClusterClient</code>
	 */
	public RedisClient getClient() {
		return client;
	}

	/**
	 * Get a new cluster connection
	 * 
	 * @return a new cluster connection
	 */
	public StatefulRedisConnection<String, String> getConnection() {
		return client.connect();
	}

	/**
	 * Create a new <code>RedisCluster</code> instance. <code>RedisClusterClient</code> is initialized
	 * and configured.
	 *
	 * @param nodes
	 *            a comma-separated list of nodes
	 * @return a new <code>RedisCluster</code> instance
	 */
	public static RedisCluster at(String nodes) {
		Objects.requireNonNull(nodes);
		RedisClient client = RedisClient.create(nodes);
		return new RedisCluster(client);
	}
}
