package com.somecompany.factservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class that provides a bean of default RestTemplate for remote API calls.
 */
@Configuration
public class RestTemplateConfig
{
	/**
	 * Bean definition constructing RestTemplate.
	 *
	 * @return a RestTemplate instance with default configuration
	 */
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}
}
