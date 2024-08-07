package com.somecompany.factservice.cache;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Record used for keeping fact related data cached in-memory.
 *
 * @param fact        a text representing a fact
 * @param originalUrl the long form original url pointing to a fact from Useless Fact API.
 * @param shortUrl    the short version of the long url, internally mapped via in-memory cache.
 * @param accessCount the atomic counter used to track access of the short url for redirection to original long url.
 */
public record FactCacheDTO(String fact, String originalUrl, String shortUrl, AtomicInteger accessCount)
{
	/**
	 * Overloaded constructor to create a FactCacheDTO initializing the access counter at 0 by default.
	 *
	 * @param fact        a text representing a fact
	 * @param originalUrl the long form original url pointing to a fact from Useless Fact API.
	 * @param shortUrl    the short version of the long url, internally mapped via in-memory cache.
	 */
	public FactCacheDTO(String fact, String originalUrl, String shortUrl)
	{
		this(fact, originalUrl, shortUrl, new AtomicInteger());
	}
}
