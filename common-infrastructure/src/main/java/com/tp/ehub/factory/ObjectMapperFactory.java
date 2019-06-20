package com.tp.ehub.factory;

import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Copy paste from Trasys code. 
 * 
 * TODO: Refactor 
 *
 */
public class ObjectMapperFactory {

	public static ObjectMapper newInstance() {
		ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
		objectMapper.getFactory().disable(Feature.CANONICALIZE_FIELD_NAMES);
				
		return objectMapper;
	}
	
	private ObjectMapperFactory() {}
	
}