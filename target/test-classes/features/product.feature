Feature: Product Price API

  Scenario: Create a new product
    Given I create a new product with:
      | brandId  | productId | priceList | priority | price | currency | startDate           | endDate             |
      | 1        | 40000     | 2         | 1        | 50.00 | EUR      | 2023-06-14T00:00:00 | 2023-12-31T23:59:59 |
    Then the product should be successfully created

  Scenario: Get applicable price for the newly created product
    Given the product with product ID "40000" and brand ID "1" exists
    When I request the price for product ID "40000" and brand ID "1" at "2023-06-14T10:00:00"
    Then the response price should be "50.00" EUR
