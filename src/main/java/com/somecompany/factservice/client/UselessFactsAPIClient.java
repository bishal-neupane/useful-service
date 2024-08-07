package com.somecompany.factservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.somecompany.factservice.exception.FactServiceClientException;
import com.somecompany.factservice.exception.FactServiceServerException;
import com.somecompany.factservice.exception.RateLimitedException;
import com.somecompany.factservice.model.UselessFact;

import io.github.resilience4j.retry.Retry;

/**
 * Service class to interact with the Useless Facts API.
 * This class fetches random facts and applies retry logic in case of failures including rate limit issues.
 */
@Service
public class UselessFactsAPIClient
{
	private static final String API_URL = "https://uselessfacts.jsph.pl/api/v2/facts/random?language=en";

	private final RestTemplate restTemplate;
	private final Retry retry;

	/**
	 * Constructs a UselessFactsAPIClient with the provided RestTemplate and Retry instance.
	 *
	 * @param restTemplate the RestTemplate to use for HTTP requests
	 * @param retry        the Retry instance to apply retry logic
	 */
	public UselessFactsAPIClient(RestTemplate restTemplate, Retry retry)
	{
		this.restTemplate = restTemplate;
		this.retry = retry;
	}

	/**
	 * Fetches a random useless fact from the Useless Facts API.
	 * Applies retry logic in case of failures.
	 *
	 * @return a {@link UselessFact} record containing the random fact, permalink and id
	 * @throws RateLimitedException       when encountered HttpClientErrorException.TooManyRequests
	 * @throws FactServiceClientException when encountered HttpClientErrorException
	 * @throws FactServiceServerException when encountered HttpServerErrorException
	 */
	public UselessFact fetchUselessFact()
	{
		return retry.executeSupplier(() -> {
			try
			{
				return restTemplate.getForObject(API_URL, UselessFact.class);
			}
			catch (HttpClientErrorException.TooManyRequests e)
			{
				throw new RateLimitedException("Useless Fact API Rate limit exceeded. Try later please.", e);
			}
			catch (HttpClientErrorException e)
			{
				throw new FactServiceClientException("Useless Fact API client error.", e.getStatusCode(), e);
			}
			catch (HttpServerErrorException e)
			{
				throw new FactServiceServerException("Useless Fact API server error", e.getStatusCode(), e);
			}
		});
	}
}
