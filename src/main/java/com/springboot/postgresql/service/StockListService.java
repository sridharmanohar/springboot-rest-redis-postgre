package com.springboot.postgresql.service;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.postgresql.EntityNotFoundException;
import com.springboot.postgresql.model.StockList;
import com.springboot.postgresql.repo.StockListRepo;

@Service
public class StockListService {

  private static final Logger LOG = LoggerFactory.getLogger(StockListService.class);
  
  @Autowired
  private StockListRepo stockListRepo;
  
  public StockList showStockInfo(String code) throws EntityNotFoundException {
    StockList stockList = stockListRepo.findByStockCode(code);
    if(stockList == null) {
      LOG.info("About to throw exception");
      throw new EntityNotFoundException(StockList.class, "stockcode", code);
    }
    else {
      LOG.info("Safe return..");
      return stockList;
    }
      
  }
  
}
