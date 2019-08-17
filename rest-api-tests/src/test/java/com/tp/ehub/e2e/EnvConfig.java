package com.tp.ehub.e2e;

import javax.ws.rs.core.UriBuilder;

public class EnvConfig {

	public static final String EHUB_API_BASE_URI = System.getProperty("ehub.ApiBaseUri", "http://localhost:8080/gateway");

	public static UriBuilder EHubApiUriBuilder() {
		return UriBuilder.fromUri(EHUB_API_BASE_URI);
	}
}