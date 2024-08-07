package com.somecompany.factservice.controller;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.somecompany.factservice.model.AccessStat;
import com.somecompany.factservice.model.FactResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the FactServiceController.
 * <p>
 * This class uses Spring Boot's {@code @SpringBootTest} annotation to load the application context
 * and configure the web environment for integration testing.
 * The {@code TestRestTemplate} is used to perform HTTP requests and verify the responses.
 * </p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FactServiceControllerIntegrationTest
{
	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * Tests the /facts POST endpoint for valid response.
	 */
	@Test
	@DisplayName("/facts post request is executed successfully.")
	void uselessFact()
	{
		ResponseEntity<FactResponse> response = restTemplate.postForEntity("/facts", null, FactResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(Objects.requireNonNull(response.getBody()).originalFact()).isNotBlank();
		assertThat(Objects.requireNonNull(response.getBody()).shortenedUrl()).isNotBlank();
	}

	/**
	 * Tests /facts/{shortUrl} GET endpoint and response of 404 when the mapping for the provided shortUrl does not exist.
	 */
	@Test
	@DisplayName("Resource NOT Found when short url does not exist")
	void redirectToOriginalFact_notFound()
	{
		ResponseEntity<String> response = restTemplate.getForEntity("/facts/jibberish", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).contains("Missing cached data");
	}

	/**
	 * Tests /facts/{shortUrl} GET endpoint when short url exists in cache.
	 */
	@Test
	@DisplayName("Successful PERMANENT_REDIRECT when short url exists")
	void redirectToOriginalFact_ok()
	{
		ResponseEntity<FactResponse> factResponse = restTemplate.postForEntity("/facts", null, FactResponse.class);
		String shortUrl = Objects.requireNonNull(factResponse.getBody()).shortenedUrl();

		ResponseEntity<Void> response = restTemplate.getForEntity("/facts/" + shortUrl, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PERMANENT_REDIRECT);
	}

	/**
	 * Tests /admin/statistics GET endpoint to be blocked by UNAUTHORIZED http status code without valid authentication.
	 */
	@Test
	@DisplayName("Unauthorized access to /admin/statistics")
	void adminStatistics_1()
	{
		ResponseEntity<AccessStat> response = restTemplate.getForEntity("/admin/statistics", AccessStat.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Tests /admin/statistics GET endpoint with correct behavior when authenticated with user details.
	 */
	@Test
	@DisplayName("Authorized access to /admin/statistics")
	void adminStatistics_2()
	{
		ResponseEntity<List> response = restTemplate.withBasicAuth("admin", "admin").getForEntity("/admin/statistics", List.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}