package com.somecompany.factservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.somecompany.factservice.cache.FactCacheDTO;
import com.somecompany.factservice.cache.FactCacheService;
import com.somecompany.factservice.client.UselessFactsAPIClient;
import com.somecompany.factservice.model.AccessStat;
import com.somecompany.factservice.model.FactResponse;
import com.somecompany.factservice.model.UselessFact;
import com.somecompany.factservice.service.urlshortener.URLShortenerService;

/**
 * Service class for handling operations related to facts.
 * This includes fetching facts, shortening URLs, caching them and maintaining access statistics.
 */
@Service
public class FactService
{
	private final UselessFactsAPIClient uselessFactAPIClient;
	private final URLShortenerService urlShortenerService;
	private final FactCacheService factCacheService;

	/**
	 * Constructs FactService using provided {@link UselessFactsAPIClient}, {@link URLShortenerService} and {@link FactCacheService}
	 *
	 * @param uselessFactAPIClient client api for fetching random fact data from Useless Fact API
	 * @param urlShortener         the URLShortenerService to transform long url into shorter version
	 * @param factCacheService     the FactCacheService instance to hold {@link FactCacheDTO}
	 */
	public FactService(UselessFactsAPIClient uselessFactAPIClient, URLShortenerService urlShortener, FactCacheService factCacheService)
	{
		this.uselessFactAPIClient = uselessFactAPIClient;
		this.urlShortenerService = urlShortener;
		this.factCacheService = factCacheService;
	}

	/**
	 * Fetches a random fact and generates a shortened URL for it.
	 * Only new facts trigger computation of the short url, otherwise existing mapping is used.
	 *
	 * @return a FactResponse object containing fact text and shortened url
	 */
	public FactResponse fetchAndProcessUselessFact()
	{
		UselessFact uselessFact = uselessFactAPIClient.fetchUselessFact();

		String shortUrl = urlShortenerService.getOrCreateShortUrl(uselessFact.permalink());

		cacheFactData(uselessFact, shortUrl);
		return new FactResponse(uselessFact.text(), shortUrl);
	}

	/**
	 * Constructs and inserts FactCacheDTO into the FactCacheService instance cache.
	 * Only the first insert is persisted for a given short url.
	 *
	 * @param uselessFact response object from the Useless Fact API holding fact metadata
	 * @param shortUrl    constructed short url that maps to cached fact data
	 */
	private void cacheFactData(UselessFact uselessFact, String shortUrl)
	{
		FactCacheDTO factCacheDTO = new FactCacheDTO(uselessFact.text(), uselessFact.permalink(), shortUrl);
		factCacheService.insertIfMissing(factCacheDTO);
	}

	/**
	 * Increments the access count for short urls held in the cache and returns original long url.
	 *
	 * @param shortUrl short url mapped to a cached fact data
	 * @return a String object referring to the original long url pointing to the Useless Fact API
	 */
	public String incrementAccessAndGetLongUrl(String shortUrl)
	{
		factCacheService.incrementAccessCount(shortUrl);
		return factCacheService.getLongUrl(shortUrl);
	}

	/**
	 * Collects and transforms FactCacheDTOs into AccessStat instances.
	 *
	 * @return list of AccessStat instances holding access count values for each short url in the cache
	 */
	public List<AccessStat> getAllAccessStatistics()
	{
		List<AccessStat> accessStats = new ArrayList<>();
		for (final FactCacheDTO cacheDTO : factCacheService.getAll())
		{
			int accessCount = cacheDTO.accessCount().get();
			accessStats.add(new AccessStat(cacheDTO.shortUrl(), accessCount));
		}
		return accessStats;
	}
}
