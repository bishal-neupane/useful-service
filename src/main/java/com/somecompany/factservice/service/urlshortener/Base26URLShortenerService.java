package com.somecompany.factservice.service.urlshortener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Base26 based URLShortenerService implementation.
 */
@Service
public class Base26URLShortenerService implements URLShortenerService
{
	private static final Logger logger = LoggerFactory.getLogger(Base26URLShortenerService.class);

	/**
	 * Atomic counter to be used for encoding process for each new url to be encoded.
	 */
	private final AtomicInteger uniqueUrlCounter = new AtomicInteger(1);

	/**
	 * A map holding already computed longUrl to shortUrl relationships, in order to avoid redundant mappings and computations.
	 */
	final Map<String, String> longUrlToShortUrl = new ConcurrentHashMap<>();

	/**
	 * Implementation of the interface method to shorten a given long url.
	 * Uses an atomic counter to encode given url if it has already not been encoded.
	 * Mapping of long url to short url is atomically computed and tracked.
	 *
	 * @param longUrl full form long url to be encoded into a shorter form
	 * @return returns existing or newly computed shorter version of the given long url
	 * @throws IllegalArgumentException when the long url is invalid
	 */
	@Override
	public String getOrCreateShortUrl(String longUrl)
	{
		validateLongUrl(longUrl);
		return longUrlToShortUrl.computeIfAbsent(longUrl, k -> {
			String encodedUrl = UrlShortenerUtil.encodeBase26(uniqueUrlCounter.getAndIncrement());
			logger.info("Computed shortUrl to longUrl mapping: [%s -> %s]".formatted(encodedUrl, longUrl));
			return encodedUrl;
		});
	}

	/**
	 * Validates that the long url is not null or empty
	 *
	 * @param longUrl the long url
	 * @throws IllegalArgumentException thrown when the long url is null or empty
	 */
	private void validateLongUrl(String longUrl)
	{
		if (longUrl == null || longUrl.trim().isEmpty())
		{
			throw new IllegalArgumentException("Invalid longUrl.");
		}
	}
}
