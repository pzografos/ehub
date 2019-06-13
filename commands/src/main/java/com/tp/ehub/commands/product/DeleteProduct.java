package com.tp.ehub.commands.product;

import java.util.UUID;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.BuilderVisibility;
import org.immutables.value.Value.Style.ImplementationVisibility;

import com.tp.ehub.common.types.Command;

/**
 * Create an existing product
 *
 */
@Value.Style(visibility = ImplementationVisibility.PACKAGE, builderVisibility = BuilderVisibility.PACKAGE, depluralize = true, jdkOnly = true)
@Value.Immutable
public interface DeleteProduct extends Command {

	/**
	 * Get the product UUID of the message
	 * 
	 * @return the product UUID of the message
	 */
	UUID product();

}
