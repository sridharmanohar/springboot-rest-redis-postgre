package com.springboot.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
/**
 * The purpose of this class is to have enough fields to hold all error info that 
 * occurs during REST calls for this application.
 * 
 * @author sridhar
 *
 */

public class ApiError {

  /** Logger Init. */
  private static final Logger LOG = LoggerFactory.getLogger(ApiError.class);
  
  /**
   * Holds operation call status. Can be anything from 4XX to signalize
   * client errors or 5XX to mean server errors.
   */
  private HttpStatus status;
  /** Holds a user-friendly message. */
  private String message;
  /** Holds a system message describing the error in detail. */ 
  private String debugMessage;
  
  /**
   * Constructor to init. only HttpStatus.
   * 
   * @param status - HttpStatus
   */
  public ApiError(final HttpStatus status) {
    super();
    LOG.debug("inside apierror status const.");
    this.status = status;
  }

  /**
   * Const. to init. HttpStatus, Message (static) and Exception detail.
   * @param status - HttpStatus
   * @param e - Exception
   */
  public ApiError(final HttpStatus status, final Throwable excp) {
    super();
    LOG.debug("inside apierror status and throwable const.");
    this.status = status;
    this.message = "unexpected error";
    this.debugMessage = excp.getLocalizedMessage();
  }

  /**
   * Const. to init. HttpStatus, Message (dynamic) and Exception detail.
   * 
   * @param status - HttpStatus
   * @param message - User-friendly Message
   * @param e - Exception
   */
  public ApiError(final HttpStatus status, final String message, final Throwable excp) {
    super();
    LOG.debug("inside apierror status, message and throwable const.");
    this.status = status;
    this.message = message;
    this.debugMessage = excp.getLocalizedMessage();
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(final HttpStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public String getDebugMessage() {
    return debugMessage;
  }

  public void setDebugMessage(final String debugMessage) {
    this.debugMessage = debugMessage;
  }
  
  
  
  
  
}
