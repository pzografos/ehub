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
import com.tp.ehub.common.infra.redis.RedisCluster;

import io.lettuce.core.api.sync.RedisCommands;

@ApplicationScoped
public class RequestHandler {

	private static int timeout = 5 * 60;

	@Inject
	Logger log;

	@Inject
	RedisCluster redisCluster;

	@Inject
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
			
			sync.setex(redisKey, timeout, redisValue);

			return request;

		} catch (JsonProcessingException e) {
			log.error("Failed to create request. Reason {}", e.getMessage());
			return null;
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
			return empty();
		}
	}
	
	public void acceptRequest(UUID requestId) {
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		String redisKey = getKeyForId(requestId);
				
		try {
			
			String redisValue = sync.get(redisKey);
			
			if (nonNull(redisValue)) {
			
				Request request = mapper.readValue(redisValue, Request.class);
				request.setStatus(Status.ACCEPTED);
				
				String updatedValue = mapper.writeValueAsString(request);
				
				sync.del(redisKey);
				sync.setex(redisKey, timeout, updatedValue);
			
			} else {
				log.error("Invalid request key  {}", redisKey);
			}

		} catch (IOException e) {
			log.error("Failed to update request. Reason {}", e.getMessage());
		}
	}
	
	public void failRequest(UUID requestId, String message) {
		
		RedisCommands<String, String> sync = redisCluster.getConnection().sync();
		
		String redisKey = getKeyForId(requestId);

		try {
			
			String redisValue = sync.get(redisKey);
			
			if (nonNull(redisValue)) {

				Request request = mapper.readValue(redisValue, Request.class);
				request.setStatus(Status.FAILED);
				request.setMessage(message);
				
				String updatedValue = mapper.writeValueAsString(request);

				sync.del(redisKey);
				sync.setex(redisKey, timeout, updatedValue);
			
			} else {
				throw new IllegalArgumentException(format("Invalid request key %s", redisKey));
			}

		} catch (IOException e) {
			log.error("Failed to update request. Reason {}", e.getMessage());	
		}
	}
	
	private String getKeyForId(UUID requestId) {
		return format("Request-%s", requestId);
	}
}
