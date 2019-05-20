package com.springboot.postgresql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
/**
 * Starting point for Spring Boot. 
 * @author sridhar
 *
 */

@SpringBootApplication
@EnableCaching
public class MainApp {

  /**
   * Main method. 
   * @param args param
   */
  public static void main(final String[] args) {
    SpringApplication.run(MainApp.class, args);
  }

}
