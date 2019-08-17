package com.tp.ehub.e2e;

import static net.serenitybdd.rest.SerenityRest.then;

import net.thucydides.core.annotations.Step;

public class RestCommonSteps {

    @Step
    public void the_system_responds_with_status(int statusCode) {
		then().statusCode(statusCode);
    }
}
