package com.somecompany.factservice.exception;

/**
 * Unchecked exception thrown when rate limited by downstream API
 */
public class RateLimitedException extends RuntimeException
{
	/**
	 * Constructs a RateLimitedException with the specified detail message
	 *
	 * @param message the detail message explaining the reason for the exception
	 * @param e throwable
	 */
	public RateLimitedException(String message, Throwable e)
	{
		super(message, e);
	}
}
