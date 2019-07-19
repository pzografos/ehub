package com.tp.ehub.common.domain.messaging.container;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.asList;

import java.lang.reflect.Method;

final class StringCreatorExtractor {

	private StringCreatorExtractor() {
	}

	static Method stringCreator(Class<?> keyType) {
		for (Method method : keyType.getMethods()) {
			if (asList("valueOf", "fromString", "from").contains(method.getName()) 
					&& method.getParameterCount() == 1 
					&& method.getParameterTypes()[0].equals(String.class)
					&& method.getReturnType().equals(keyType) 
					&& isStatic(method.getModifiers())) {
				return method;
			}
		}

		return null;
	}
}