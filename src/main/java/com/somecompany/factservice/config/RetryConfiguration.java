package com.somecompany.factservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

/**
 * Configuration class that provides a bean for wrapping and executing retry logic.
 * Resilience4j retry mechanism is configured.
 */
@Configuration
public class RetryConfiguration
{
	private static final int MAX_ATTEMPTS = 5;
	private static final int EXP_BACKOFF_MULTIPLIER = 2;
	private static final int EXP_BACKOFF_INTERVAL_MILLIS = 2000;

	/**
	 * RetryRegistry instance bean definition with exponential backoff strategy.
	 *
	 * @return RetryRegistry bean with given configuration settings
	 */
	@Bean
	public RetryRegistry retryRegistry()
	{
		RetryConfig retryConfig = RetryConfig.custom()
			.maxAttempts(MAX_ATTEMPTS)
			.intervalFunction(IntervalFunction.ofExponentialBackoff(EXP_BACKOFF_INTERVAL_MILLIS, EXP_BACKOFF_MULTIPLIER))
			.build();
		return RetryRegistry.of(retryConfig);
	}

	/**
	 * Retry instance for executing retry.
	 *
	 * @param registry RetryRegistry instance pre-configured with settings
	 * @return Retry instance for wrapping and executing retry logic
	 */
	@Bean
	public Retry retry(RetryRegistry registry)
	{
		return registry.retry("Backoff Retry");
	}
}
