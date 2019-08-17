Feature: Manage products

	Background:
		Given user "UserA" is a manager of company "CompanyA"
	
	Scenario: User with manager role adds a new valid product to the company
 		Given user "UserA" requests product with code "code1"
 		And no product is found
		When user "UserA" requests to add a new product with code "code1"
		Then the system responds with 201
		
	Scenario: User with manager role adds a product with existing code to the company
 		Given user "UserA" requests product with code "code1"
 		And a product is found
		When user "UserA" requests to add a new product with code "code1"
		Then the system responds with 400		
