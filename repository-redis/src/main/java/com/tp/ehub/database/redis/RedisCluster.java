package com.tp.ehub.database.redis;


import static io.lettuce.core.cluster.SlotHash.getSlot;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions.RefreshTrigger;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode.NodeFlag;

/**
 * Redis cluster representation.
 *
 * <p>Maintains the cluster hosts and a thread-safe {@link RedisClusterClient}
 *
 * @author Dimitris Mandalidis
 *
 */
public class RedisCluster {

	private final RedisClusterClient client;

	private RedisCluster(RedisClusterClient client) {
		this.client = client;
	}

	/**
	 * Get the {@link RedisClusterClient}
	 * @return the underlying {@link RedisClusterClient}
	 */
	public RedisClusterClient client() {
		return client;
	}

	/**
	 * Get a new cluster connection
	 * @return a new cluster connection
	 */
	public StatefulRedisClusterConnection<String,String> clusterConnection() {
		return client.connect();
	}

	/**
	 * Get a single node connection for the given hash key. The cluster is queried
	 * for the owner of the given hash key and a connection to that node is returned
	 *
	 * <p>{@link RedisClusterClient} does not support cross-slot transactions and neither
	 * delegates such calls to the owning node of a slot. Therefore, cluster clients need
	 * to find the master node owning a specific slot in order to open a transaction. So,
	 * this method is used for finding the owning node of a Redis key slot and provide a
	 * connection to it.
	 *
	 * @param statefulRedisClusterConnection the cluster connection to derive the node connection from
	 * @param key the key for which the owning master node which be queried
	 * @return a new single node connection
	 */
	@SuppressWarnings("resource")
	@Deprecated
	public StatefulRedisConnection<String, String> nodeConnectionFor(
		StatefulRedisClusterConnection<String, String> statefulRedisClusterConnection,
		String key) {
		return client.getPartitions().stream()
			.filter(node -> node.is(NodeFlag.MASTER))
			.filter(node -> node.hasSlot(getSlot(key)))
			.findFirst()
			.map(node -> statefulRedisClusterConnection.getConnection(node.getNodeId()))
			.<IllegalArgumentException>orElseThrow(IllegalArgumentException::new)
			;
	}

	@Deprecated
	public StatefulRedisConnection<String, String> nodeConnectionFor(String key) {
		StatefulRedisClusterConnection<String, String> statefulRedisClusterConnection = this.client.connect();
		return nodeConnectionFor(statefulRedisClusterConnection, key);
	}

	public NodeCommandSyncExecutor nodeCommandSyncExecutor(StatefulRedisClusterConnection<String, String> statefulRedisClusterConnection){
		return new NodeCommandSyncExecutor(this, statefulRedisClusterConnection, 2);
	}

	public NodeCommandSyncExecutor nodeCommandSyncExecutor(){
		StatefulRedisClusterConnection<String, String> statefulRedisClusterConnection = this.client.connect();
		return this.nodeCommandSyncExecutor(statefulRedisClusterConnection);
	}

	/**
	 * Create a new {@link RxRedis} instance. {@link RedisClusterClient} is initialized and configured.
	 *
	 * <p>Topology is automatically refreshed when a change is noticed (e.g. a node is down).
	 *
	 * @param nodes a comma-separated list of nodes
	 * @return a new {@link RxRedis} instance
	 */
	public static RedisCluster at(String nodes) {
		Objects.requireNonNull(nodes);

		String[] n = nodes.split(",\\s*");
		List<RedisURI> nodeList = asList(n).stream().map(RedisURI::create).collect(toList());

		RedisClusterClient clusterClient = RedisClusterClient.create(nodeList);

		ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
			.enableAdaptiveRefreshTrigger(RefreshTrigger.ASK_REDIRECT, RefreshTrigger.MOVED_REDIRECT, RefreshTrigger.PERSISTENT_RECONNECTS)
			.enablePeriodicRefresh(Duration.ofMinutes(5))
			.enablePeriodicRefresh(true)
			.build();

		ClusterClientOptions clientOptions = ClusterClientOptions.builder()
			.topologyRefreshOptions(topologyRefreshOptions)
			.build();

		clusterClient.setOptions(clientOptions);

		return new RedisCluster(clusterClient);
	}

	public static final class NodeCommandSyncExecutor {
		private static final Logger LOGGER = LoggerFactory.getLogger(NodeCommandSyncExecutor.class);

		private final int retries;
		private final RedisCluster redisCluster;
		private final StatefulRedisClusterConnection<String, String> statefulRedisClusterConnection;

		public <R> R call(String redisKey, Function<StatefulRedisConnection<String, String>, R> command) throws Exception {
			return this.doExecute(redisKey, command, this.retries);
		}

		public <R> R exec(String redisKey, java.util.function.Function<StatefulRedisConnection<String, String>, R> command) {
			try {
				return this.doExecute(redisKey, command::apply, this.retries);
			} catch (Exception e) {
				//cast is guaranteed to succeed since passed command does not throw checked exceptions
				//avoid wrapping RuntimeException unnecessarily
				throw (RuntimeException) e;
			}
		}

		private <R> R doExecute(String redisKey, Function<StatefulRedisConnection<String, String>, R> command, int retriesRemaining) throws Exception {
			try {
				return command.apply(this.redisCluster.nodeConnectionFor(this.statefulRedisClusterConnection, redisKey));
			} catch (RedisCommandExecutionException exception) {
				if (retriesRemaining > 0 && exception.getMessage().contains("MOVED") || exception.getMessage().contains("ASK")) {
					LOGGER.warn(format("topology changed, refreshing and retrying operation, retries remaining %d", retriesRemaining), exception);
					this.redisCluster.client().reloadPartitions();
					return doExecute(redisKey, command, retriesRemaining - 1);
				}
				throw exception;
			}
		}

		private NodeCommandSyncExecutor(RedisCluster redisCluster,
										StatefulRedisClusterConnection<String, String> statefulRedisClusterConnection,
										int retries) {
			this.redisCluster = redisCluster;
			this.statefulRedisClusterConnection = statefulRedisClusterConnection;
			this.retries = retries;
		}
	}
}
