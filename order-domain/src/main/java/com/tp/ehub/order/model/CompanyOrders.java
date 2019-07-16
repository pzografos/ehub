package com.tp.ehub.order.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tp.ehub.common.domain.model.Entity;

public class CompanyOrders implements Entity<UUID> {

	private UUID companyId;

	private Map<UUID, Order> orders = new HashMap<UUID, Order>();
	
	private Map<UUID, Long> stock = new HashMap<UUID, Long>();

	public CompanyOrders(UUID companyId) {
		this.companyId = companyId;
	}

	@Override
	public UUID getId() {
		return companyId;
	}

	public Map<UUID, Order> getOrders() {
		return orders;
	}

	public Map<UUID, Long> getStock() {
		return stock;
	}
	
	public void updateStock(UUID productId, Long quantity) {
		stock.put(productId, quantity);
	}

}
