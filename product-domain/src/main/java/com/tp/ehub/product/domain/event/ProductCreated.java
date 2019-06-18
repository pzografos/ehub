package com.tp.ehub.product.domain.event;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Product is created
 */
public final class ProductCreated implements ProductEvent{

	private OffsetDateTime timestamp;

	private UUID product;
	
	private UUID company;
	
	private String code;
	

	public void company(UUID company) {
		this.company = company;
	}
	
	/**
	 * Get the company UUID of the message
	 * 
	 * @return the company UUID of the message
	 */
	public UUID company() {
		return company;
	}

	public void code(String code) {
		this.code = code;
	}

	/**
	 * Get the product code of the message
	 * 
	 * @return the product code of the message
	 */
	public String code() {
		return code;
	}
	
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
