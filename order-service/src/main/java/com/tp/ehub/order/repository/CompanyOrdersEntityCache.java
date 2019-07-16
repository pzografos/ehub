package com.tp.ehub.order.repository;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.Dependent;

import com.tp.ehub.common.domain.repository.RootEntityCache;
import com.tp.ehub.order.model.CompanyOrders;

/**
 * Temp implementation
 * <p>
 * TODO: replace by Redis implementation
 */
@Dependent
public class CompanyOrdersEntityCache implements RootEntityCache<UUID, CompanyOrders> {

	@Override
	public Optional<CompanyOrders> get(UUID id) {
		return Optional.empty();
	}

	@Override
	public void cache(CompanyOrders entity) {

	}

	@Override
	public void evict(UUID id) {

	}

}
