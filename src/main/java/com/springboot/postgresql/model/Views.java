package com.springboot.postgresql.model;

/**
 * Purpose of this class is to create views, to be used by jsonviews to send
 * specific fields in the json response.
 * 
 * @author sridhar
 *
 */
public class Views {

  /**
   * Whatever fields in the model marked with this view name with the annot.
   * 
   * @JsonView will only be part of the json response marked with this view name
   *           in the controller.
   * 
   * @author sridhar
   *
   */
  public static class Public {
  }

  /**
   * Whatever fields in the model marked with this view name with the annot.
   * 
   * @JsonView will only be part of the json response marked with this view name
   *           in the controller.
   * 
   * @author sridhar
   *
   */
  public static class Internal extends Public {
  }
  
  public static class StockView {
    
  }
}
