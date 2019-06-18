package com.tp.ehub.product.domain.event;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Product is deleted
 */
public final class ProductDeleted implements ProductEvent{
	
	private OffsetDateTime timestamp;
	
	private UUID product;

	public void timestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public OffsetDateTime timestamp() {
		return timestamp;
	}

	public void product(UUID product) {
		this.product = product;
	}
	
	@Override
	public UUID product() {
		return product;
	}

}
