package com.springboot.postgresql.repo;

import com.springboot.postgresql.model.ExchangeList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This class performs persistence layer integration testing.
 * @author sridhar
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RepoIntegrationTests {

  /** Repo instance. */
  @Autowired
  private ExchangeListRepo exchangeListRepo;

  /**
   * Verifies:
   * 1. Only Parents i.e. Exchanges are fetched.
   * 2. Count of exchanges is correct.
   */
  @Test
  public void whenFindAllParents_thenSizeShouldBeSize() {
    final List<Object[]> objList = exchangeListRepo.findAllParents();
    Assertions.assertThat(objList.size()).isEqualTo(5);
  }

  /**
   * Verifies:
   * 1. Count of properties of Exchanges is correct.
   * 2. And they are not null.
   */
  @Test
  public void whenFindAllParents_thenNoDataIsNull() {
    final List<Object[]> objList = exchangeListRepo.findAllParents();
    for (final Object[] o : objList) {
      Assertions.assertThat(o[0]).isNotNull();
      Assertions.assertThat(o[1]).isNotNull();
      Assertions.assertThat(o[2]).isNotNull();
    }
  }

  /**
   * Verifies:
   * 1. Only Exchange info is fetched (and not stock lists).
   * 2. Count of parameters fetched for each exchange is correct.
   */
  @Test
  public void whenFindAllParents_thenShowOnlyParents() {
    final List<Object[]> objList = exchangeListRepo.findAllParents();
    for (final Object[] o : objList) {
      Assertions.assertThat(o.length).isEqualTo(3);
    }
  }

  /**
   * Verifies that Exchange and their Stock details are fetched.
   */
  @Test
  public void whenFindAll_thenShowStockLists() {
    final List<ExchangeList> elList = exchangeListRepo.findAll();
    for (final ExchangeList el : elList) {
      Assertions.assertThat(el.getStockLists()).asList();
    }
  }

}
