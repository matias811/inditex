Feature: Product Price API

  Scenario: Get applicable price for a product
    Given the product with ID "35455" and brand ID "1" exists
    When I request the price for product ID "35455" and brand ID "1" at "2020-06-14T10:00:00"
    Then the response price should be "35.50" EUR
