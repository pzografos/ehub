package com.tp.ehub.common.infra.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Custom jackson annotation applied to every message of E-HUB
 * 
 * <p>
 * The cases that {@code JsonMessage} tries to address are the following:
 * <ul>
 * <li>Messages originated from Kafka may have additional properties, apparent
 * in previous API versions which may not be needed by the current API version.
 * Such properties should not fail during deserialization and <em>it is assumed
 * that their omission by the current API version was intended.</em></li>
 * <li>Messages originated from the codebase may not need a specific property
 * compared to a previous API version. Such properties should not be serialized
 * as in most cases the other end have assigned a default value in such a case
 * and have not set them as optional.</li>
 * </ul>
 * 
 * @see JsonInclude
 * @see JsonIgnoreProperties
 *
 */
@JacksonAnnotationsInside
@JsonInclude(value = Include.NON_NULL)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
public @interface JsonMessage {

}