package com.somecompany.factservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the useful Fact Service Spring Boot Application.
 * This application exposes endpoints to fetch random facts from the Useless Fact API,
 * provides a shortened URL for each fetched fact, caches them, and also offers private area to consult access statistics.
 */
@SpringBootApplication
public class FactServiceApplication
{
	/**
	 * Main method to run the useful Fact Service application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		SpringApplication.run(FactServiceApplication.class, args);
	}
}
