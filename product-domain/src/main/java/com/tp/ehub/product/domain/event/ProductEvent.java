package com.tp.ehub.product.domain.event;

import java.util.UUID;

import com.tp.ehub.common.types.Event;

public interface ProductEvent extends Event{

	/**
	 * Get the product UUID of the message
	 * 
	 * @return the product UUID of the message
	 */
	UUID id();
	
}
