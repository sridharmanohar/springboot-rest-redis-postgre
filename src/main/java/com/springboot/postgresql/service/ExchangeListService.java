package com.springboot.postgresql.service;

import com.springboot.postgresql.model.ExchangeList;
import com.springboot.postgresql.repo.ExchangeListRepo;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Layer - ExchangeList.
 * 
 * @author sridhar
 *
 */

@Service
public class ExchangeListService {

  /** Init. Logger */
  private static final Logger LOG = LoggerFactory.getLogger(ExchangeListService.class);
  
  /** ExchangeListRepo Dependency - IOC. */
  @Autowired
  private ExchangeListRepo exchangeListRepo;

  /**
   * Makes a call to the repo method to fetch all exchanges and the
   * stocks associated with each exchange.
   * Forwards this list to the controller.
   *  
   * @return list
   */
  
  
  @Cacheable(value="first-cache")
//  @CacheEvict(value="first-cache", allEntries=true)
//  @Transactional  
  public List<ExchangeList> showAllExchangesandStocks() {
    LOG.debug("making a call to repo...");
    return exchangeListRepo.findAll();
  }

  /**
   * Process the data fetched from the repo. method and form List of ExchangeLists.
   * 
   * @return
   */
  public List<ExchangeList> showAllExchanges() {
    final List<ExchangeList> exchList = new ArrayList<>();
    for (final Object[] o : exchangeListRepo.findAllParents()) {
      exchList.add(new ExchangeList(o[0].toString(), o[1].toString(), o[2].toString()));
    }
    return exchList;
  }

}
