package com.tp.ehub.e2e.product;

import static net.serenitybdd.rest.SerenityRest.given;

import java.util.UUID;

import com.tp.ehub.e2e.EnvConfig;
import com.tp.ehub.e2e.product.dto.CreateProductRequest;

import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;

public class ProductSteps {

	@Step
	public Response getProduct(UUID companyId, String code) {
		return given()
			.contentType("application/json")
		.when()
		.get(EnvConfig.EHubApiUriBuilder().path("api/v1/").path(companyId.toString()).path("product/code/").path(code).build());				
	}
	
	@Step
	public void createProduct(CreateProductRequest createProductRequest) {
		given()
			.contentType("application/json")
			.body(createProductRequest)
		.when()
		.post(EnvConfig.EHubApiUriBuilder().path(String.format("api/v1/%s/product", createProductRequest.getCompanyId().toString())).build());
	}
}
