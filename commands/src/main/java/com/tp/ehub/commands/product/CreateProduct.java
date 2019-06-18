package com.tp.ehub.commands.product;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.tp.ehub.common.types.Command;

/**
 * Create a new product
 *
 */
public class CreateProduct implements Command {
	
	private OffsetDateTime timestamp;
	
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

}
