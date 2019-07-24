package com.tp.ehub.order.repository;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.common.domain.repository.EntityCache;
import com.tp.ehub.order.model.Order;

/**
 * Temp implementation
 * <p>
 * TODO: replace by Redis implementation
 */
@Dependent
public class OrderEntityCache implements EntityCache<UUID, Order> {

	@Override
	public Optional<Order> get(UUID id) {
		return Optional.empty();
	}

	@Override
	public void cache(Order entity) {

	}

	@Override
	public void evict(UUID id) {

	}

}
