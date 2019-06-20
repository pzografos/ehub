package com.tp.ehub.product.model.event;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.tp.ehub.model.event.AbstractEvent;

public abstract class ProductEvent extends AbstractEvent<UUID> {

	protected UUID productId;
	
	private UUID companyId;
	
	private ZonedDateTime timestamp;

	protected ProductEvent() {

	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	@Override
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}
