package com.springboot.postgresql.repo;

import com.springboot.postgresql.model.ExchangeList;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * Persistence Layer for ExchangeList.
 * 
 * @author sridhar
 *
 */
@org.springframework.stereotype.Repository
public interface ExchangeListRepo extends Repository<ExchangeList, Integer> {

  /** Fetches all Exchanges and their children. */
  List<ExchangeList> findAll();

  /** Fetches exchange code, name and country of all exchanges. */
  @Query("select e.exchangeCode, e.name, e.country from ExchangeList e")
  List<Object[]> findAllParents();
}
