package com.tp.ehub.product.domain.event;

import java.util.UUID;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.BuilderVisibility;
import org.immutables.value.Value.Style.ImplementationVisibility;

/**
 * Product is created
 */
@Value.Style(visibility = ImplementationVisibility.PACKAGE, builderVisibility = BuilderVisibility.PACKAGE, depluralize = true, jdkOnly = true)
@Value.Immutable
public interface ProductCreated extends ProductEvent{

	/**
	 * Get the company UUID of the message
	 * 
	 * @return the company UUID of the message
	 */
	UUID company();

	/**
	 * Get the product code of the message
	 * 
	 * @return the product code of the message
	 */
	String code();
}
