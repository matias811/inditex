# Price Service API for INDITEX test

This is a Spring Boot application that provides a REST API to retrieve the applicable price for a product based on the given product ID, brand ID, and application date.

## Project Setup

### Prerequisites
- Java 17 or higher
- Maven
- H2 Database (in-memory)
- Spring Boot 2.5.x or higher

### Installation

1. Clone the repository:
   git clone git@github.com:matias811/inditex.git

2. Navigate to the project directory:
   cd <project-directory>

3. Build the project with Maven:
   mvn clean install

4. Run the application:
   mvn spring-boot:run

The application will start on `http://localhost:8080`.

## API Documentation

### Base URL

The base URL for the API is:

http://localhost:8080

### Endpoints

#### 1. **Get Price**
This endpoint returns the applicable price for a given product ID, brand ID, and application date.

- **Endpoint**: `/get-price`
- **Method**: `GET`
- **Query Parameters**:
  - `productId` (Integer) - The ID of the product (e.g., `35455`)
  - `brandId` (Integer) - The ID of the brand (e.g., `1` for ZARA)
  - `applicationDate` (String) - The date and time in ISO 8601 format (`yyyy-MM-dd'T'HH:mm:ss`)

- **Response**: 
  - If a price is found, the response will contain the price details in JSON format.
  - If no applicable price is found, a `204 No Content` response will be returned.

##### Example Request:
curl -X GET "http://localhost:8080/get-price?productId=35455&brandId=1&applicationDate=2020-06-14T10:00:00"

##### Example Response (when a price is found):
{
  "id": 1,
  "brandId": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "priceList": 1,
  "productId": 35455,
  "priority": 0,
  "price": 35.50,
  "currency": "EUR"
}

##### Example Response (when no price is found):
{
  "message": "No price found for the given criteria."
}

## Tests

You can test the API with the following cases:

### Test 1: Request at 10:00 on June 14 for product 35455 and brand 1 (ZARA).
curl -X GET "http://localhost:8080/get-price?productId=35455&brandId=1&applicationDate=2020-06-14T10:00:00"

### Test 2: Request at 16:00 on June 14 for product 35455 and brand 1 (ZARA).
curl -X GET "http://localhost:8080/get-price?productId=35455&brandId=1&applicationDate=2020-06-14T16:00:00"

### Test 3: Request at 21:00 on June 14 for product 35455 and brand 1 (ZARA).
curl -X GET "http://localhost:8080/get-price?productId=35455&brandId=1&applicationDate=2020-06-14T21:00:00"

### Test 4: Request at 10:00 on June 15 for product 35455 and brand 1 (ZARA).
curl -X GET "http://localhost:8080/get-price?productId=35455&brandId=1&applicationDate=2020-06-15T10:00:00"

### Test 5: Request at 21:00 on June 16 for product 35455 and brand 1 (ZARA).
curl -X GET "http://localhost:8080/get-price?productId=35455&brandId=1&applicationDate=2020-06-16T21:00:00"

## Database Schema

This application uses an H2 in-memory database to store price data.

### Price Table

| Column Name   | Data Type       | Description                                            |
|---------------|-----------------|--------------------------------------------------------|
| `id`          | `BIGINT`        | The unique identifier for the price entry.             |
| `brandId`     | `INTEGER`       | The ID of the brand.                                   |
| `startDate`   | `TIMESTAMP`     | The start date and time when the price becomes valid.  |
| `endDate`     | `TIMESTAMP`     | The end date and time when the price is no longer valid.|
| `priceList`   | `INTEGER`       | The price list identifier.                             |
| `productId`   | `INTEGER`       | The ID of the product.                                 |
| `priority`    | `INTEGER`       | The priority of the price (higher values have higher priority). |
| `price`       | `DECIMAL(10, 2)`| The price value.                                       |
| `currency`    | `VARCHAR(3)`    | The currency of the price.                             |

### Example Prices

| id  | brandId | startDate           | endDate             | priceList | productId | priority | price | currency |
|-----|---------|---------------------|---------------------|-----------|-----------|----------|-------|----------|
| 1   | 1       | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1         | 35455     | 0        | 35.50 | EUR      |
| 2   | 1       | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2         | 35455     | 1        | 25.45 | EUR      |
| 3   | 1       | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3         | 35455     | 1        | 30.50 | EUR      |
| 4   | 1       | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4         | 35455     | 1        | 38.95 | EUR      |

## Logging

This application uses **SLF4J** and **Logback** for logging. The logs are printed to the console for easier debugging.

### Example Logs

Price Controller - Received request with productId=35455, brandId=1, applicationDate=2020-06-14T16:00
Price Service - Starting call to find applicable prices.
Price Service - Price recovered : 25.45
Price Controller - Found price: 25.45

## Conclusion

This application provides a simple and efficient way to retrieve the applicable prices for products based on specific parameters like the product ID, brand ID, and application date. The use of the H2 in-memory database makes it easy to test and develop locally without the need for a full-scale database setup.
