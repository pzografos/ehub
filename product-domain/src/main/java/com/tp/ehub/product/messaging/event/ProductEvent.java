package com.tp.ehub.product.messaging.event;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.infra.messaging.AbstractEvent;
import com.tp.ehub.common.infra.serialization.JsonMessage;

@JsonMessage
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = ProductCreated.class, name = "Product.Created"), 
				@Type(value = ProductDeleted.class, name = "Product.Deleted") })
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

	@JsonIgnore
	@Override
	public UUID getKey() {
		return companyId;
	}

}
