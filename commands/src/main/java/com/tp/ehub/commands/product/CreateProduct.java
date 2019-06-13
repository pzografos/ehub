package com.tp.ehub.commands.product;

import java.util.UUID;

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
