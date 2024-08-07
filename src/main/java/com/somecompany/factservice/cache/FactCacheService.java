package com.somecompany.factservice.cache;

import java.util.Collection;

/**
 * Interface for a simple cache to hold and manipulate FactCacheDTOs
 */
public interface FactCacheService
{
	/**
	 * Inserts provided FactCacheDTO into the cache if missing.
	 *
	 * @param factCacheDTO record holding fact data.
	 */
	void insertIfMissing(FactCacheDTO factCacheDTO);

	/**
	 * Increments access count of the short url.
	 *
	 * @param shortUrl shorter form of the long url.
	 */
	void incrementAccessCount(String shortUrl);

	/**
	 * Returns corresponding longer version of the shorter url stored in the cache.
	 *
	 * @param shortUrl shorter form of the long url.
	 * @return original longer version of the shorter url.
	 */
	String getLongUrl(String shortUrl);

	/**
	 * Returns a {@link Collection} of all the FactCacheDTOs stored in the cache.
	 *
	 * @return collection of FactCacheDTO entries in the cache.
	 */
	Collection<FactCacheDTO> getAll();
}
