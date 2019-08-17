package com.tp.ehub.e2e;

import org.junit.BeforeClass;

import io.restassured.RestAssured;

public class ApiTestSuite {

	@BeforeClass
	public static void setUp() throws Exception {
		RestAssured.useRelaxedHTTPSValidation();
	}
}
