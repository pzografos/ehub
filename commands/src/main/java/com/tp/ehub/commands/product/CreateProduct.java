package com.tp.ehub.commands.product;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.BuilderVisibility;
import org.immutables.value.Value.Style.ImplementationVisibility;

import com.tp.ehub.common.types.Command;

/**
 * Create a new product
 *
 */
@Value.Style(visibility = ImplementationVisibility.PACKAGE, builderVisibility = BuilderVisibility.PACKAGE, depluralize = true, jdkOnly = true)
@Value.Immutable
public interface CreateProduct extends Command {

	/**
	 * Get the product code of the message
	 * 
	 * @return the product code of the message
	 */
	String code();
}
