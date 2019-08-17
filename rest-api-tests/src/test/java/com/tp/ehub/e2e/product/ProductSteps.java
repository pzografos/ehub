package com.tp.ehub.e2e.product;

import static net.serenitybdd.rest.SerenityRest.given;

import com.tp.ehub.e2e.EnvConfig;
import com.tp.ehub.e2e.product.dto.CreateProductRequest;

import net.thucydides.core.annotations.Step;

public class ProductSteps {

	@Step
	public void createProduct(CreateProductRequest createProductRequest) {
		given()
			.contentType("application/json")
			.body(createProductRequest)
		.when()
		.post(EnvConfig.EHubApiUriBuilder().path("api/v1/product").build());
	}
}
