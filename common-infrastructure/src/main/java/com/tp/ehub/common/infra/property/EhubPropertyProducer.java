package com.tp.ehub.common.infra.property;

import java.util.Objects;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class EhubPropertyProducer {

	private Properties properties = new Properties();

	@EhubProperty
	@Produces
	public String produceString(final InjectionPoint ip) {
		return this.properties.getProperty(getKey(ip));
	}

	@EhubProperty
	@Produces
	public Integer produceInt(final InjectionPoint ip) {
		String property = this.properties.getProperty(getKey(ip));
		if (Objects.nonNull(property)) {
			return Integer.valueOf(property);
		}
		return null;
	}

	@EhubProperty
	@Produces
	public Boolean produceBoolean(final InjectionPoint ip) {
		String propertyValue = this.properties.getProperty(getKey(ip));
		if (Objects.nonNull(propertyValue)) {
			return Boolean.valueOf(propertyValue);
		}
		return null;
	}

	private String getKey(final InjectionPoint ip) {
		return (ip.getAnnotated().isAnnotationPresent(EhubProperty.class) && !ip.getAnnotated().getAnnotation(EhubProperty.class).value().isEmpty())
				? ip.getAnnotated().getAnnotation(EhubProperty.class).value()
				: ip.getMember().getName();
	}

	@PostConstruct
	public void init() {
		this.properties.putAll(System.getProperties());
		this.properties.putAll(System.getenv());
	}
}
