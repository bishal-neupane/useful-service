package com.somecompany.factservice.client;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.somecompany.factservice.model.UselessFact;

import io.github.resilience4j.retry.Retry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit test for UselessFactsAPIClient fetch method.
 */
@ExtendWith(MockitoExtension.class)
class UselessFactsAPIClientTest
{
	@Mock
	RestTemplate restTemplate;

	@Mock
	Retry retry;

	@InjectMocks
	UselessFactsAPIClient uselessFactsAPIClient;

	@Test
	void fetchUselessFact()
	{
		// given
		var expectedFact = new UselessFact("123", "A random fact.", "www.factapi.com/123");
		when(restTemplate.getForObject(anyString(), any(Class.class))).thenReturn(expectedFact);
		when(retry.executeSupplier(any(Supplier.class))).thenAnswer(input -> {
			Supplier<UselessFact> supplier = input.getArgument(0);
			return supplier.get();
		});

		// when
		UselessFact actualFact = uselessFactsAPIClient.fetchUselessFact();

		// then
		assertNotNull(actualFact);
		assertThat(actualFact.text()).isEqualTo(expectedFact.text());
	}
}
