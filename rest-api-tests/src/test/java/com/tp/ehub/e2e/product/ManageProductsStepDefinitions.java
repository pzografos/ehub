package com.tp.ehub.e2e.product;

import static java.lang.String.format;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.ehub.e2e.RestCommonSteps;
import com.tp.ehub.e2e.product.dto.CreateProductRequest;
import com.tp.ehub.e2e.users.UserSteps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

public class ManageProductsStepDefinitions {
		
    private final Logger logger = LoggerFactory.getLogger(ManageProductsStepDefinitions.class);
    
    @Steps
    private ProductSteps product;
    
    @Steps
    private UserSteps users;
    
    @Steps
    private RestCommonSteps rest;

    @Given("user {string} is a manager of company {string}")
    public void user_is_a_manager_of_company(String user, String company) {
    	users.user_is_a_manager_of_company(user, company);
    }

    @Given("user {string} requests product with code {string}")
    public void user_requests_product_with_code(String user, String code) {
    	logger.info(format("Given user '%s' requests product with code '%s'", user, code));
    }

    @Given("no product is found")
    public void no_product_is_found(){
    	logger.info("no product is found");
    	//rest.the_system_responds_with_status(404);
    }
    
    @Given("a product is found")
    public void a_product_is_found(){
    	logger.info("a product is found");
    	//rest.the_system_responds_with_status(200);
    }
    
    @When("user {string} requests to add a new product with code {string}")
    public void user_requests_to_add_a_new_product_with_code_to_company(String user, String code) {
    	
    	logger.info(format("When user '%s' requests to add a new product with code '%s'", user, code));
    	
		CreateProductRequest createProductRequest = new CreateProductRequest();
		createProductRequest.setCode(code);
		createProductRequest.setCompanyId(users.getCompany(user));
		createProductRequest.setDescription("some description");
		createProductRequest.setName(UUID.randomUUID().toString());
		createProductRequest.setQuantity(1L);
		
		product.createProduct(createProductRequest);
    }
    
    @Then("the system responds with {int}")
    public void the_system_responds_with(int statusCode) {
    	rest.the_system_responds_with_status(statusCode);
    }
   
}
