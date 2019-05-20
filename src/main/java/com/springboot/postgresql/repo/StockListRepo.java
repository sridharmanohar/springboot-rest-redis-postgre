package com.springboot.postgresql.repo;

import org.springframework.data.repository.Repository;

import com.springboot.postgresql.model.StockList;

public interface StockListRepo extends Repository<StockList, Integer> {

  StockList findByStockCode(String code);
  
}
