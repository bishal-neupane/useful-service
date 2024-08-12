package com.somecompany.factservice.service.urlshortener;

/**
 * Url shortening util class.
 */
public class UrlShortenerUtil
{
	private static final String BASE26_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final int BASE = BASE26_CHARACTERS.length();

	/**
	 * Private constructor safeguarding instantiation of this utility class.
	 */
	private UrlShortenerUtil()
	{
		throw new IllegalStateException("Utility class with only static members.");
	}

	/**
	 * Encodes a given number into a base26 String
	 *
	 * @param num integer value to be encoded
	 * @return a base26 encoded string for the given integer
	 */
	public static String encodeBase26(int num)
	{
		var encoded = new StringBuilder();
		while (num > 0)
		{
			int remainder = num % BASE;
			num /= BASE;
			encoded.append(BASE26_CHARACTERS.charAt(remainder));
		}
		return encoded.reverse().toString();
	}
}
