package com.somecompany.factservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.somecompany.factservice.config.RestTemplateConfig;
import com.somecompany.factservice.config.RetryConfiguration;
import com.somecompany.factservice.config.SecurityConfig;

/**
 * Integration test for the main application.
 * Verifies the application context is loaded without errors and java config beans are created as well.
 */
@SpringBootTest
class FactServiceApplicationTests
{
	private static final Logger logger = LoggerFactory.getLogger(FactServiceApplicationTests.class);

	@Autowired
	RetryConfiguration retryConfiguration;

	@Autowired
	RestTemplateConfig restTemplateConfig;

	@Autowired
	SecurityConfig securityConfig;

	/**
	 * Tests context loads and all config beans are initialized by spring
	 */
	@Test
	void contextLoads()
	{
		logger.info("Spring application context loaded successfully.");
		Assertions.assertNotNull(retryConfiguration);
		Assertions.assertNotNull(restTemplateConfig);
		Assertions.assertNotNull(securityConfig);
	}
}
