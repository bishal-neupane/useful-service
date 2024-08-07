package com.somecompany.factservice.service.urlshortener;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the Base26URLShortenerService
 * Tests the functionality of generating and managing short urls given long urls.
 */
class Base26URLShortenerServiceTest
{
	URLShortenerService urlShortenerService = new Base26URLShortenerService();

	/**
	 * Verifies correct encoding of initial long url
	 */
	@Test
	@DisplayName("Url encoded successfully.")
	void shortenUrl_1()
	{
		var longUrl = "https://uselessfacts.jsph.pl/api/v2/facts/9fbb54b2da8115bf6d69bdafc5118bb2";
		String shortUrl = urlShortenerService.getOrCreateShortUrl(longUrl);
		
		assertFalse(shortUrl.isBlank());
		assertThat(shortUrl).isEqualTo("b");
	}

	/**
	 * Tests only unique long urls are encoded.
	 */
	@Test
	@DisplayName("Only unique urls are encoded")
	void shortenUrl_3()
	{
		urlShortenerService.getOrCreateShortUrl("https://uselessfacts.jsph.pl/api/v2/facts/1");
		urlShortenerService.getOrCreateShortUrl("https://uselessfacts.jsph.pl/api/v2/facts/2");
		urlShortenerService.getOrCreateShortUrl("https://uselessfacts.jsph.pl/api/v2/facts/3");

		IntStream.range(1, 100).forEach((ignore) -> urlShortenerService.getOrCreateShortUrl("https://uselessfacts.jsph.pl/api/v2/facts/2"));
		assertThat(((Base26URLShortenerService) urlShortenerService).longUrlToShortUrl).hasSize(3);
	}

	/**
	 * Verifies all unique urls are encoded.
	 */
	@Test
	@DisplayName("All unique urls are encoded")
	void shortenUrl_4()
	{
		IntStream.range(1, 100).forEach((i) -> urlShortenerService.getOrCreateShortUrl("https://uselessfacts.jsph.pl/api/v2/facts/" + i));
		assertThat(((Base26URLShortenerService) urlShortenerService).longUrlToShortUrl).hasSize(99);
	}

	/**
	 * Verifies exceptions thrown for invalid long urls
	 */
	@Test
	@DisplayName("throws exception when url to encode is empty or null")
	void shortenUrl_2()
	{
		assertThrows(IllegalArgumentException.class, () -> urlShortenerService.getOrCreateShortUrl(null));
		assertThrows(IllegalArgumentException.class, () -> urlShortenerService.getOrCreateShortUrl("   "));
	}
}
