package com.springboot.postgresql.controller;

import com.fasterxml.jackson.annotation.JsonView;

import com.springboot.postgresql.EntityNotFoundException;
import com.springboot.postgresql.model.ExchangeList;
import com.springboot.postgresql.model.StockList;
import com.springboot.postgresql.model.Views;
import com.springboot.postgresql.service.ExchangeListService;
import com.springboot.postgresql.service.StockListService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class. Handles all requests from the web.
 * 
 * @author sridhar
 *
 */
@RestController
public class TestController {

  /** Logger Init. */
  private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
  
  /** Service class instance. */
  @Autowired
  private ExchangeListService exchangeListService;

  @Autowired
  private StockListService stockListService;
  
  /**
   * Handles requests to show all exchanges and their stocks.
   * 
   * @return JSON Response.
   */
  @GetMapping("/showAll")
  public List<ExchangeList> showAllExchangesandStocks() {
    return exchangeListService.showAllExchangesandStocks();
  }

  /**
   * Handles requests to show all exchanges info.
   * 
   * @return json response.
   */
  @JsonView(Views.Public.class)
  @GetMapping("/showAllExchanges")
  public List<ExchangeList> showAllExchanges() {
    return exchangeListService.showAllExchanges();
  }

  /**
   * Handles requests to show stock specific info.
   * 
   * @param code - Stock Code
   * @return {@link StockList}
   * @throws EntityNotFoundException  - When no such stock is found
   */
  @JsonView(Views.StockView.class)
  @GetMapping("/stock/{stockCode}")
  public StockList showStockInfo(@PathVariable(name = "stockCode") final String code) 
      throws EntityNotFoundException {
    LOG.debug("request for:",code);
    return stockListService.showStockInfo(code);
  }
}
