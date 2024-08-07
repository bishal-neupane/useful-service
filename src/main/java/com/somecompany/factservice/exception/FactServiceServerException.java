package com.somecompany.factservice.exception;

import org.springframework.http.HttpStatusCode;

/**
 * Custom exception to represent server side errors during remote http calls in the Fact Service.
 * Extends {@link RuntimeException} and includes the HTTP status code of the error.
 */
public class FactServiceServerException extends RuntimeException
{
	/**
	 * HttpStatusCode for the exception
	 */
	private final HttpStatusCode statusCode;

	/**
	 * Constructs a new FactServiceServerException with the specified detail message, HTTP status code, and cause.
	 *
	 * @param msg        the detail message explaining the reason for the exception
	 * @param statusCode the HTTP status code associated with the error
	 * @param e          the cause of the exception
	 */
	public FactServiceServerException(String msg, HttpStatusCode statusCode, Throwable e)
	{
		super(msg, e);
		this.statusCode = statusCode;
	}

	/**
	 * Gets the HTTP status code associated with this exception.
	 *
	 * @return the HTTP status code
	 */
	public HttpStatusCode getStatusCode()
	{
		return statusCode;
	}
}
