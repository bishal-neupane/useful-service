package com.somecompany.factservice.exception;

/**
 * Unchecked exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException
{
	/**
	 * Constructs a ResourceNotFoundException with the specified detail message
	 *
	 * @param message the detail message explaining the reason for the exception
	 */
	public ResourceNotFoundException(String message)
	{
		super(message);
	}
}
