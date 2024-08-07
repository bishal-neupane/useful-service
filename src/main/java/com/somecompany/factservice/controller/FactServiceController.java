package com.somecompany.factservice.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.somecompany.factservice.model.AccessStat;
import com.somecompany.factservice.model.FactResponse;
import com.somecompany.factservice.service.FactService;

/**
 * REST controller for handling operations related to facts.
 * Provides endpoints for fetching and processing useless facts, redirecting to original facts,
 * and retrieving access statistics.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class FactServiceController
{
	private final FactService factService;

	/**
	 * Constructs a new FactServiceController with the given FactService.
	 *
	 * @param uselessFactService the service to handle fact-related operations
	 */
	public FactServiceController(FactService uselessFactService)
	{
		this.factService = uselessFactService;
	}

	/**
	 * Fetches a random useless fact and processes it to provide a shortened URL.
	 * This endpoint is mapped to POST requests at /facts.
	 *
	 * @return a ResponseEntity containing the FactResponse with the original fact and shortened URL as json
	 */
	@PostMapping(value = "/facts", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<FactResponse> uselessFact()
	{
		return ResponseEntity.ok(factService.fetchAndProcessUselessFact());
	}

	/**
	 * Redirects to the original fact URL based on the given shortened URL.
	 * Increments the access count for the given shortened URL.
	 * This endpoint is mapped to GET requests at /facts/{shortenedUrl}.
	 *
	 * @param shortenedUrl the shortened URL of the fact
	 * @return a ResponseEntity with empty body, permanent redirect status code and redirect to original long url
	 */
	@GetMapping(value = "/facts/{shortenedUrl}")
	public ResponseEntity<Void> redirectToOriginalFact(@PathVariable String shortenedUrl)
	{
		String originalLongUrl = factService.incrementAccessAndGetLongUrl(shortenedUrl);
		return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).header(HttpHeaders.LOCATION, originalLongUrl).build();
	}

	/**
	 * Retrieves access statistics for all shortened URLs.
	 * This endpoint is private and is mapped to GET requests at /admin/statistics.
	 * Basic Authentication with username and password is required to access this endpoint.
	 *
	 * @return a ResponseEntity containing a list of AccessStat objects with access statistics
	 */
	@GetMapping(value = "/admin/statistics")
	public ResponseEntity<List<AccessStat>> adminStatistics()
	{
		return ResponseEntity.ok(factService.getAllAccessStatistics());
	}
}
