package com.tp.ehub.order.messaging.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Order.StockChanged")
public class StockChanged extends OrderEvent {

	public static final String NAME = "STOCK_CHANGED";
	
	private UUID productId;
	
	private Long quantity;

	public StockChanged(UUID companyId) {
		super(companyId);
	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getEventName() {
		return NAME;
	}

}
