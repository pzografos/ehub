package com.tp.ehub.common.infra.repository.redis;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class RedisClusterProducer {

	@Produces @ApplicationScoped
	public RedisCluster get() {
		return RedisCluster.at(System.getenv("redis_nodes"));
	}
}
