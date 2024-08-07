package com.somecompany.factservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record representing access statistics for a shortened URL.
 * Contains information about the shortened URL and the number of times it has been accessed.
 *
 * @param shortenedUrl The shortened URL for the fact.
 * @param accessCount  The number of times the shortened URL has been accessed.
 */
public record AccessStat(@JsonProperty("shortened_url") String shortenedUrl, @JsonProperty("access_count") Integer accessCount)
{

}
