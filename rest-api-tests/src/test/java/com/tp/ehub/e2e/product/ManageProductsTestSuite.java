package com.tp.ehub.e2e.product;

import org.junit.runner.RunWith;

import com.tp.ehub.e2e.ApiTestSuite;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features"
)
public class ManageProductsTestSuite extends ApiTestSuite{
	
}
