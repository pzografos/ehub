package com.tp.ehub.product.messaging.commands;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.exception.BusinessException;
import com.tp.ehub.common.domain.function.CheckedBiFunction;
import com.tp.ehub.common.domain.messaging.AbstractCommand;
import com.tp.ehub.common.domain.messaging.JsonMessage;

/**
 * The <code>Command</code> represents a trigger for the system. In its most
 * common form, it represents a user request.
 *
 */
@JsonMessage
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ 
		@Type(value = CreateProductCommand.class, name = CreateProductCommand.NAME),
		@Type(value = DeleteProductCommand.class, name = DeleteProductCommand.NAME),
		@Type(value = UpdateProductStockCommand.class, name = UpdateProductStockCommand.NAME)
})
public abstract class ProductCommand extends AbstractCommand<UUID> {
		
	protected UUID companyId;
	
	protected UUID productId;

	public UUID getCompanyId() {
		return companyId;
	}

	public void setCompanyId(UUID companyId) {
		this.companyId = companyId;
	}
	
	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}
	
	@Override
	@JsonIgnore
	public UUID getKey() {
		return companyId;
	}

	protected abstract <P, R> R map(P parameter, BiFunctionVisitor<P, R> mapper) throws BusinessException;
	
	public interface BiFunctionVisitor<P, R> extends CheckedBiFunction<P, ProductCommand, R> {

		@Override
		default R apply(P parameter, ProductCommand command) throws BusinessException{
			return command.map(parameter, this);
		}

		default R visit(P parameter, CreateProductCommand command) throws BusinessException {return fallback(parameter, command);}
		default R visit(P parameter, DeleteProductCommand command) throws BusinessException {return fallback(parameter, command);}
		default R visit(P parameter, UpdateProductStockCommand command) throws BusinessException {return fallback(parameter, command);}

		R fallback(P parameter, ProductCommand command) throws BusinessException;
	}
}
