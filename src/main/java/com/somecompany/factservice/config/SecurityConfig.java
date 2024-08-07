package com.somecompany.factservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application.
 * Spring Security's web security support and integration with Spring MVC is enabled.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig
{
	/**
	 * Configures the security filter chain for the application.
	 *
	 * <p>This configuration ensures that the "/admin/statistics" endpoint is protected and requires authentication,
	 * while all other endpoints are publicly accessible. Basic HTTP authentication is enabled, and CSRF protection is disabled.</p>
	 * UserDetails for authentication is passed via application properties.
	 *
	 * @param http the {@link HttpSecurity} to configure
	 * @return the configured {@link SecurityFilterChain}
	 * @throws Exception if an error occurs while configuring the security filter chain
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/admin/statistics").authenticated().anyRequest().permitAll())
			.httpBasic(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable);
		return http.build();
	}
}
