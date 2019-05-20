package com.springboot.postgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * This is a custom exception class.
 *  
 * @author sridhar
 *
 */

public class EntityNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;
  
  /** Logger Init. */
  private static final Logger LOG = LoggerFactory.getLogger(EntityNotFoundException.class);

  /**
   * 
   * @param clazz
   * @param key
   * @param value
   */
  public EntityNotFoundException(Class<?> clazz, String key, String value) {
    super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), key, value));
    LOG.debug("inside entitynotfoundexception constructor");
  }
  
  /**
   * Creates the error message that is part of the Response Body.
   * 
   * @param entity
   * @param key
   * @param value
   * @return
   */
  private static String generateMessage(final String entity, final String key, final String value) {
    LOG.debug("inside generateMessage");
    return StringUtils.capitalize(entity) + " was not found for parameters {"+key+"="+value+"}";
  }
  
}
