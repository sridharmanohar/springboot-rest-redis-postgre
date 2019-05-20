package com.springboot.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This is the class that gets called whenever an exception is thrown 
 * by the application.
 * The ControllerAdvice intercepts any exception thrown by the application.
 * 
 * @author sridhar
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

  /** Logger Init. */
  private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class); 
  
  /**
   * This method handles EntityNotFoundException which is signified
   * by the ExceptionHandler annot.
   * Returns ResponsEntity which spring converts into a message 
   * using message converters.
   * @param ex - Type of Exception
   * @return
   */
  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(final EntityNotFoundException excp) {
    LOG.debug("inside handleEntityNotFound");
    final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
    apiError.setMessage(excp.getMessage());
    return buildResponseEntity(apiError);    
  }
  
  private ResponseEntity<Object> buildResponseEntity(final ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
