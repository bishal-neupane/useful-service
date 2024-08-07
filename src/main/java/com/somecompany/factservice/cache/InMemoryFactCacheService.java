package com.somecompany.factservice.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.somecompany.factservice.exception.ResourceNotFoundException;

/**
 * In-memory implementation of FactCacheService.
 */
@Service
public class InMemoryFactCacheService implements FactCacheService
{
	/**
	 * Map data structure to hold short url and cache data relationship.
	 * ConcurrentHashMap implementation is used to handle thread-safe put, read operations efficiently.
	 */
	private final Map<String, FactCacheDTO> shortUrlToFactCacheDTO = new ConcurrentHashMap<>();

	/**
	 * Inserts a FactCacheDTO to the map if the short url is not already present.
	 *
	 * @param factCacheDTO record with fact related data
	 */
	@Override
	public void insertIfMissing(FactCacheDTO factCacheDTO)
	{
		shortUrlToFactCacheDTO.putIfAbsent(factCacheDTO.shortUrl(), factCacheDTO);
	}

	/**
	 * Validates shortUrl and increments the associated access counter by 1 when present.
	 *
	 * @param shortUrl the short version of the original long url from the Useless Fact API
	 * @throws IllegalArgumentException  when the short url is null or empty
	 * @throws ResourceNotFoundException when there is no corresponding FactCacheDTO or associated data
	 */
	@Override
	public void incrementAccessCount(String shortUrl)
	{
		validate(shortUrl);
		shortUrlToFactCacheDTO.get(shortUrl).accessCount().incrementAndGet();
	}

	/**
	 * Validates shortUrl and returns the associated long url.
	 *
	 * @param shortUrl the short version of the original long url from the Useless Fact API
	 * @throws IllegalArgumentException  when the short url is null or empty
	 * @throws ResourceNotFoundException when there is no corresponding FactCacheDTO or associated data
	 */
	@Override
	public String getLongUrl(String shortUrl)
	{
		validate(shortUrl);
		return shortUrlToFactCacheDTO.get(shortUrl).originalUrl();
	}

	/**
	 * Provides a {@link Collection} view of the values contained in the map.
	 *
	 * @return collection of FactCacheDTOs stored in the map
	 */
	@Override
	public Collection<FactCacheDTO> getAll()
	{
		return shortUrlToFactCacheDTO.values();
	}

	/**
	 * Validates the shortUrl.
	 *
	 * @param shortUrl the short version of the original long url from the Useless Fact API
	 * @throws IllegalArgumentException  when the short url is null or empty
	 * @throws ResourceNotFoundException when there is no corresponding FactCacheDTO or associated data
	 */
	private void validate(String shortUrl)
	{
		if (shortUrl == null || shortUrl.isEmpty())
		{
			throw new IllegalArgumentException("Invalid shortUrl supplied.");
		}

		FactCacheDTO factCacheDTO = shortUrlToFactCacheDTO.get(shortUrl);
		if (factCacheDTO == null || factCacheDTO.originalUrl() == null || factCacheDTO.accessCount() == null)
		{
			throw new ResourceNotFoundException("Missing cached data for short url: " + shortUrl);
		}
	}
}
