package com.somecompany.factservice.service.urlshortener;

/**
 * Interface for generating shorter versions of long urls.
 */
public interface URLShortenerService
{
	/**
	 * Generates a shorter version of the long form url if not existent.
	 *
	 * @param longUrl long url to be encoded into a shorter form
	 * @return shorter representation of the longUrl
	 */
	String getOrCreateShortUrl(String longUrl);
}
