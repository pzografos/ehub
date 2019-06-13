package com.tp.ehub.product.domain.event;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.BuilderVisibility;
import org.immutables.value.Value.Style.ImplementationVisibility;

/**
 * Product is deleted
 */
@Value.Style(visibility = ImplementationVisibility.PACKAGE, builderVisibility = BuilderVisibility.PACKAGE, depluralize = true, jdkOnly = true)
@Value.Immutable
public interface ProductDeleted extends ProductEvent{

}
