package com.tp.ehub.product.messaging;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.tp.ehub.common.domain.messaging.container.AbstractMessageContainer;
import com.tp.ehub.product.messaging.commands.ProductCommand;

@ApplicationScoped
@Named("product-commands")
public class ProductCommandsMessageContainer extends AbstractMessageContainer<UUID, ProductCommand> {

	@Inject
	public ProductCommandsMessageContainer() {
		super("product-commands", UUID.class, ProductCommand.class);
	}

}