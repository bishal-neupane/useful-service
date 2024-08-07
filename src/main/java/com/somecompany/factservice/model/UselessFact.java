package com.somecompany.factservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record holding the data retrieved from the Useless Fact API.
 * Among other fields only the fields listed below are consumed.
 *
 * @param id        The id of the factual data from the Fact API
 * @param text      The random fact in plaintext
 * @param permalink The original long url pointing to the fact
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UselessFact(String id, String text, String permalink)
{
}
