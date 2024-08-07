package com.somecompany.factservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.somecompany.factservice.exception.FactServiceClientException;
import com.somecompany.factservice.exception.FactServiceServerException;
import com.somecompany.factservice.exception.RateLimitedException;
import com.somecompany.factservice.exception.ResourceNotFoundException;

/**
 * General exception handling controller advice.
 * Makes sure proper error codes and error messages are propagated back to the clients.
 */
@ControllerAdvice
public class GlobalExceptionHandler
{
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Handles unchecked ResourceNotFoundException.
	 *
	 * @param ex         the instance of ResourceNotFoundException
	 * @param webRequest the WebRequest instance
	 * @return a ResponseEntity with status HttpStatus.NOT_FOUND and detailed message in the body
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest)
	{
		return getErrorDetailsResponseEntity(ex.getMessage(), webRequest, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles RateLimitedException.
	 *
	 * @param ex         the instance of RateLimitedException
	 * @param webRequest the WebRequest instance
	 * @return A ResponseEntity with status HttpStatus.TOO_MANY_REQUESTS and detailed message in the body
	 */
	@ExceptionHandler(RateLimitedException.class)
	public ResponseEntity<?> handleRateLimitedException(RateLimitedException ex, WebRequest webRequest)
	{
		return getErrorDetailsResponseEntity(ex.getMessage(), webRequest, HttpStatus.TOO_MANY_REQUESTS);
	}

	/**
	 * Handles FactServiceClientException.
	 *
	 * @param ex         the instance of FactServiceClientException
	 * @param webRequest the WebRequest instance
	 * @return A ResponseEntity with a corresponding status code and detailed message in the body
	 */
	@ExceptionHandler(FactServiceClientException.class)
	public ResponseEntity<?> handleFactServiceClientException(FactServiceClientException ex, WebRequest webRequest)
	{
		return getErrorDetailsResponseEntity(ex.getMessage(), webRequest, ex.getStatusCode());
	}

	/**
	 * Handles FactServiceServerException.
	 *
	 * @param ex         the instance of FactServiceServerException
	 * @param webRequest the WebRequest instance
	 * @return A ResponseEntity with a corresponding status code and detailed message in the body
	 */
	@ExceptionHandler(FactServiceServerException.class)
	public ResponseEntity<?> handleFactServiceServerException(FactServiceServerException ex, WebRequest webRequest)
	{
		return getErrorDetailsResponseEntity(ex.getMessage(), webRequest, ex.getStatusCode());
	}

	/**
	 * Handles general exceptions when no other fine-grained exception handlers get invoked.
	 *
	 * @param ex         the exception being dealt with
	 * @param webRequest the WebRequest instance
	 * @return A ResponseEntity with status code 500 and detailed message in the body
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneralException(Exception ex, WebRequest webRequest)
	{
		return getErrorDetailsResponseEntity(ex.getMessage(), webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Constructs and returns ResponseEntity with a detailed error message and http status code.
	 * Logs the message for debugging on the server-side.
	 *
	 * @param msg        the message
	 * @param webRequest the WebRequest instance holding RequestAttributes
	 * @param statusCode HttpStatusCode representing the exception
	 * @return ResponseEntity with ErrorDetails as body
	 */
	private ResponseEntity<ErrorDetails> getErrorDetailsResponseEntity(String msg, WebRequest webRequest, HttpStatusCode statusCode)
	{
		logger.error(msg);
		var errorDetails = new ErrorDetails(statusCode.value(), msg, webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, statusCode);
	}

	/**
	 * Internal record holding information regarding the exception being handled.
	 *
	 * @param statusCode http status code
	 * @param message    error message from the exception
	 * @param details    web request description
	 */
	record ErrorDetails(int statusCode, String message, String details)
	{
	}
}
