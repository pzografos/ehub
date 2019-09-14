package com.tp.ehub.common.domain.messaging.container;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface Container {

	String name();
	
	Class<?> keyClass() default String.class;
		
	int partritions() default 15;
}
