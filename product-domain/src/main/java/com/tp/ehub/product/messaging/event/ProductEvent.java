package com.tp.ehub.product.messaging.event;

import java.util.UUID;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.function.Reducer;
import com.tp.ehub.common.domain.messaging.AbstractEvent;
import com.tp.ehub.common.domain.messaging.container.Container;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = ProductCreated.class, name = ProductCreated.NAME), 
				@Type(value = ProductDeleted.class, name = ProductDeleted.NAME),
				@Type(value = ProductStockUpdated.class, name = ProductStockUpdated.NAME) })
@Container( name = "product-events", keyClass = UUID.class)
public abstract class ProductEvent extends AbstractEvent<UUID>{

	private UUID productId;

	private UUID companyId;

	protected ProductEvent() {

	}

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}

	@JsonIgnore
	@Override
	public UUID getKey() {
		return companyId;
	}
	
	protected abstract <P, R> R map(P parameter, BiFunctionVisitor<P, R> mapper);
	
	public interface BiFunctionVisitor<P, R> extends BiFunction<P, ProductEvent, R> {

		@Override
		default R apply(P parameter, ProductEvent event) {
			return event.map(parameter, this);
		}

		default R visit(P parameter, ProductCreated event) {return fallback(parameter, event);}
		default R visit(P parameter, ProductDeleted event) {return fallback(parameter, event);}
		default R visit(P parameter, ProductStockUpdated event) {return fallback(parameter, event);}

		R fallback(P parameter, ProductEvent event);
	}
    
	public interface ReducerVisitor<S> extends BiFunctionVisitor<S, S>, Reducer<S, ProductEvent> {

        @Override
        default S fallback(S value, ProductEvent event) {return value;}
    }
}
