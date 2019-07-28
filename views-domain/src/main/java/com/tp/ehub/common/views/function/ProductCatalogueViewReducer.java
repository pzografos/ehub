package com.tp.ehub.common.views.function;

import java.util.UUID;

import com.tp.ehub.common.domain.messaging.function.ViewReducer;
import com.tp.ehub.common.views.model.ProductCatalogueView;
import com.tp.ehub.product.messaging.event.ProductEvent;
import com.tp.ehub.product.messaging.event.ProductEvent.ReducerVisitor;

public interface ProductCatalogueViewReducer extends ViewReducer<UUID, ProductEvent, UUID, ProductCatalogueView>, ReducerVisitor<ProductCatalogueView>{

}
