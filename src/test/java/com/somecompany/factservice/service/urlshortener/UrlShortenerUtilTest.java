package com.somecompany.factservice.service.urlshortener;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UrlShortenerUtil class.
 */
class UrlShortenerUtilTest
{
	@Test
	void encodeBase26()
	{
		assertThat(UrlShortenerUtil.encodeBase26(0)).isEmpty();
		assertThat(UrlShortenerUtil.encodeBase26(1)).isEqualTo("b");
		assertThat(UrlShortenerUtil.encodeBase26(2000)).isEqualTo("cyy");
		assertThat(UrlShortenerUtil.encodeBase26(20678)).isEqualTo("bepi");
	}
}