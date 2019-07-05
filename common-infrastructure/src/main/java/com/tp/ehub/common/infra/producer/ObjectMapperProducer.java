package com.tp.ehub.common.infra.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.ehub.factory.ObjectMapperFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@ApplicationScoped
public class ObjectMapperProducer {

	@Produces
	@Named("objectMapper")
	public ObjectMapper objectMapper() {
		return ObjectMapperFactory.newInstance();
	}
}
