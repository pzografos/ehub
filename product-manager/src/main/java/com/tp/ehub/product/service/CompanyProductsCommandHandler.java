package com.tp.ehub.product.service;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.tp.ehub.common.service.AbstractCommandHandler;
import com.tp.ehub.common.types.Command;
import com.tp.ehub.product.domain.event.ProductEvent;
import com.tp.ehub.product.domain.model.CompanyCatalogue;

public class CompanyProductsCommandHandler extends AbstractCommandHandler<ProductEvent, CompanyCatalogue, UUID>{

	@Override
	public BiFunction<Command, CompanyCatalogue, Collection<ProductEvent>> commandToEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Function<Command, UUID> aggregateKey() {
		return command -> command.company();
	}

}
