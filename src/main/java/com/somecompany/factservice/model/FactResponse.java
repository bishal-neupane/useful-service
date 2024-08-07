package com.somecompany.factservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record representing the response for a fetched fact to be returned to the client.
 * Contains the original fact and its corresponding shortened URL.
 *
 * @param originalFact The original useless fact retrieved from the Useless Fact API.
 * @param shortenedUrl The shortened URL generated for the original fact url.
 */
public record FactResponse(@JsonProperty("original_fact") String originalFact, @JsonProperty("shortened_url") String shortenedUrl)
{
}
