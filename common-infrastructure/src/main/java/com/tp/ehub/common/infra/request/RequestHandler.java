package com.tp.ehub.common.infra.request;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.common.domain.request.Request;
import com.tp.ehub.common.domain.request.Request.Status;
import com.tp.ehub.common.infra.repository.redis.RedisCluster;

import io.lettuce.core.api.sync.RedisCommands;

@ApplicationScoped
public class RequestHandler {

	private static int timeout = 5;

	@Inject
	Logger log;

	@Inject
	RedisCluster redisCluster;

	@Named("objectMapper")
	ObjectMapper mapper;

	public Request createRequest() {

		Request request = new Request();
		request.setId(UUID.randomUUID());
		request.setStatus(Status.PENDING);

		RedisCommands<String, String> sync = redisCluster.getConnection().sync();

		String redisKey = getKeyForId(request.getId());

		try {

			String redisValue = mapper.writeValueAsString(request);

			sync.psetex(redisKey, timeout, redisValue);

			return request;

		} catch (JsonProcessingException e) {
			log.error("Failed to create request. Reason {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public Optional<Request> getRequest(UUID requestId) {
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();

		String redisKey = getKeyForId(requestId);

		try {
			
			String redisValue = sync.get(redisKey);
			
			if (nonNull(redisValue)) {
				return of(mapper.readValue(redisValue, Request.class));
			} else {
				return empty();
			}

		} catch (IOException e) {
			log.error("Failed to update request. Reason {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public void acceptRequest(UUID requestId) {
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		String redisKey = getKeyForId(requestId);

		try {
			
			String redisValue = sync.get(redisKey);
			
			Request request = mapper.readValue(redisValue, Request.class);
			request.setStatus(Status.ACCEPTED);
			
			sync.psetex(redisKey, timeout, redisValue);

		} catch (IOException e) {
			log.error("Failed to update request. Reason {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public void failRequest(UUID requestId, String message) {
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		String redisKey = getKeyForId(requestId);

		try {
			
			String redisValue = sync.get(redisKey);
			
			Request request = mapper.readValue(redisValue, Request.class);
			request.setStatus(Status.FAILED);
			request.setMessage(message);
			
			sync.psetex(redisKey, timeout, redisValue);

		} catch (IOException e) {
			log.error("Failed to update request. Reason {}", e.getMessage());	
			throw new RuntimeException(e);
		}
	}
	
	private String getKeyForId(UUID requestId) {
		return format("Request-%s", requestId);
	}
}
