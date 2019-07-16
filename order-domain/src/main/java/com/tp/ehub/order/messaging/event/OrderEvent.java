package com.tp.ehub.order.messaging.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.infra.messaging.AbstractEvent;
import com.tp.ehub.common.infra.serialization.JsonMessage;

@JsonMessage
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = OrderCreated.class, name = "Order.Created"),
				@Type(value = OrderCancelled.class, name = "Order.Cancelled"),
				@Type(value = OrderCompleted.class, name = "Order.Completed"), 
				@Type(value = StockChanged.class, name = "Order.StockChanged") })
public abstract class OrderEvent extends AbstractEvent<UUID> {

	protected UUID companyId;

	protected OrderEvent(UUID companyId) {
		this.companyId = companyId;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	@JsonIgnore
	@Override
	public UUID getKey() {
		return companyId;
	}

}
