package com.tp.ehub.product.messaging.commands;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tp.ehub.common.domain.exception.BusinessException;
import com.tp.ehub.common.domain.function.CheckedConsumer;
import com.tp.ehub.common.domain.messaging.AbstractCommand;
import com.tp.ehub.common.domain.messaging.JsonMessage;
import com.tp.ehub.common.domain.messaging.container.Container;

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
@Container( name = "product-commands", keyClass = UUID.class)
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

	public abstract void accept(ConsumerVisitor mapper) throws BusinessException;
	
	public interface ConsumerVisitor extends CheckedConsumer<ProductCommand> {

		@Override
		default void accept(ProductCommand command) throws BusinessException {
			command.accept(this);
		}

		default void visit(CreateProductCommand command) throws BusinessException {fallback(command);}
		default void visit(DeleteProductCommand command) throws BusinessException {fallback(command);}
		default void visit(UpdateProductStockCommand command) throws BusinessException {fallback(command);}

		default void fallback(ProductCommand command) throws BusinessException {}
	}
}
