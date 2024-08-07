package com.somecompany.factservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.somecompany.factservice.cache.FactCacheDTO;
import com.somecompany.factservice.cache.FactCacheService;
import com.somecompany.factservice.client.UselessFactsAPIClient;
import com.somecompany.factservice.model.FactResponse;
import com.somecompany.factservice.model.UselessFact;
import com.somecompany.factservice.service.urlshortener.URLShortenerService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for FactService.
 * Tests and verifies interactions with other dependent services.
 */
@ExtendWith(MockitoExtension.class)
class FactServiceTest
{
	@Mock
	UselessFactsAPIClient uselessFactAPIClient;
	@Mock
	URLShortenerService urlShortenerService;
	@Mock
	FactCacheService factCacheService;

	@InjectMocks
	FactService factService;

	@Test
	@DisplayName("Fact is processed and shortened and kept into cache correctly")
	void acquireAndProcessUselessFact_1()
	{
		// given
		var fact = "Your hair collects space dust from comets";
		var uselessFact = new UselessFact("1", fact, "https://www.factapi/api/facts/1");
		when(uselessFactAPIClient.fetchUselessFact()).thenReturn(uselessFact);
		when(urlShortenerService.getOrCreateShortUrl("https://www.factapi/api/facts/1")).thenReturn("xyz");

		// when
		FactResponse uselessFactResponse = factService.fetchAndProcessUselessFact();

		// then
		assertThat(uselessFactResponse.originalFact()).isEqualTo("Your hair collects space dust from comets");
		assertThat(uselessFactResponse.shortenedUrl()).isEqualTo("xyz");

		verify(factCacheService, times(1)).insertIfMissing(any(FactCacheDTO.class));
	}

	@Test
	@DisplayName("Long url retrieved after incrementing access count")
	void incrementAccessAndGetLongUrl_2()
	{
		// given
		// when
		factService.incrementAccessAndGetLongUrl("xyz");

		// then
		verify(factCacheService, times(1)).incrementAccessCount(anyString());
		verify(factCacheService, times(1)).getLongUrl(anyString());
	}

	@Test
	@DisplayName("Access stat fetched from the cache")
	void getAllAccessStatistics()
	{
		// given: setup
		// when
		factService.getAllAccessStatistics();

		// then
		verify(factCacheService, times(1)).getAll();
	}
}
