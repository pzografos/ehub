package com.tp.ehub.order.messaging.event;

import java.util.UUID;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.function.Reducer;
import com.tp.ehub.common.domain.messaging.AbstractEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = OrderCreated.class, name = OrderCreated.NAME),
				@Type(value = OrderCancelled.class, name = OrderCancelled.NAME),
				@Type(value = OrderCompleted.class, name = OrderCompleted.NAME)})
public abstract class OrderEvent extends AbstractEvent<UUID> {

	protected UUID companyId;

	protected OrderEvent(UUID companyId) {
		this.companyId = companyId;
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
	
	public interface BiFunctionVisitor<P, R> extends BiFunction<P, OrderEvent, R> {

		@Override
		default R apply(P parameter, OrderEvent event) {
			return event.map(parameter, this);
		}

		default R visit(P parameter, OrderCreated event) {return fallback(parameter, event);}
		default R visit(P parameter, OrderCancelled event) {return fallback(parameter, event);}
		default R visit(P parameter, OrderCompleted event) {return fallback(parameter, event);}

		R fallback(P parameter, OrderEvent event);
	}
    
	public interface ReducerVisitor<S> extends BiFunctionVisitor<S, S>, Reducer<S, OrderEvent> {

        @Override
        default S fallback(S value, OrderEvent event) {return value;}
    }
}
