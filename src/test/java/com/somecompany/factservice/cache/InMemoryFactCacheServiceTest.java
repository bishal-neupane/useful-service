package com.somecompany.factservice.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.somecompany.factservice.exception.ResourceNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for InMemoryFactCacheService's functionalities related to handling of in-memory cached data.
 */
class InMemoryFactCacheServiceTest
{
	FactCacheService factCacheService = new InMemoryFactCacheService();
	
	@Test
	@DisplayName("Insert missing does not override existing values")
	void insertIfMissing()
	{
		// given
		var factCacheDTO = new FactCacheDTO("Hot water will turn into ice faster than cold water.", "www.factbase.com", "abc");
		var factCacheDTO2 = new FactCacheDTO("Some other fact.", "www.override.com", "abc");

		// when
		factCacheService.insertIfMissing(factCacheDTO);
		factCacheService.insertIfMissing(factCacheDTO2);

		// then
		List<FactCacheDTO> stored = new ArrayList<>(factCacheService.getAll());
		Assertions.assertThat(stored).hasSize(1);
		assertThat(stored.get(0)).isEqualTo(factCacheDTO);
		assertThat(stored.get(0).accessCount().get()).isZero();
		assertThat(stored.get(0).originalUrl()).isEqualTo("www.factbase.com");
	}

	@Test
	@DisplayName("Existing long url is fetched or thrown exception")
	void updateAccessAndGetLongUrl()
	{
		// given
		var factCacheDTO = new FactCacheDTO("Hot water will turn into ice faster than cold water.", "www.factbase.com", "abc");

		// when
		factCacheService.insertIfMissing(factCacheDTO);

		// then
		assertThat(factCacheService.getLongUrl("abc")).isEqualTo("www.factbase.com");
		assertThrows(ResourceNotFoundException.class, () -> factCacheService.getLongUrl("nonexistent"));
		assertThrows(IllegalArgumentException.class, () -> factCacheService.getLongUrl(""));
	}

	@Test
	@DisplayName("Access statistics is updated according to usage.")
	void getAll()
	{
		// given
		var factCacheDTO = new FactCacheDTO("Hot water will turn into ice faster than cold water.", "www.factbase.com", "abc");
		var factCacheDTO2 = new FactCacheDTO("A cloud weighs around a million tonnes.", "www.factapi.com", "def");

		// when
		factCacheService.insertIfMissing(factCacheDTO);
		factCacheService.insertIfMissing(factCacheDTO2);
		IntStream.range(0, 10).forEach((ignore) -> factCacheService.incrementAccessCount("abc"));
		factCacheService.incrementAccessCount("def");

		//then
		Map<String, AtomicInteger> statMap = factCacheService.getAll()
			.stream()
			.collect(Collectors.toMap(FactCacheDTO::shortUrl, FactCacheDTO::accessCount));
		assertThat(statMap.get("abc").get()).isEqualTo(10);
		assertThat(statMap.get("def").get()).isEqualTo(1);
	}
}