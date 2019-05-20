package com.springboot.postgresql.service;

import com.springboot.postgresql.model.ExchangeList;

import com.springboot.postgresql.repo.ExchangeListRepo;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This class tests Service Layer testing.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceIntegrationTest {

  /** Service Layer instance. */
  @Autowired
  private ExchangeListService exchangeListService;

  /** Repo instance for mocking. */
  @MockBean
  private ExchangeListRepo exchangeListRepo;

  /** Constant. */
  private static final String COUNTRY = "India";
  
  /**
   * Verifies:
   * 1.Count of exchanges matches.
   * 2.Data for each exchange.
   */ 
  @Test
  public void whenShowAllExchanges_thenShowOnlyExchanges() {
    // *********** setup start *********************//
    final Object[] obj0 = new Object[] {"NSE","National Stock Exchange",
        ServiceIntegrationTest.COUNTRY};
    final Object[] obj1 = new Object[]{"BSE","Bombay Stock Exchange",
        ServiceIntegrationTest.COUNTRY};
    final List<Object[]> liArray = new ArrayList<>();
    liArray.add(obj0);
    liArray.add(obj1);
    Mockito.when(this.exchangeListRepo.findAllParents()).thenReturn(liArray);
    // *********** setup complete *********************//

    final List<ExchangeList> exchList = exchangeListService.showAllExchanges();
    Assertions.assertThat(exchList.size()).isEqualTo(2);
    int i = 0;
    for (final ExchangeList e : exchList) {
      if (i == 0) {
        Assertions.assertThat(e.getExchangeCode()).isEqualTo("NSE");
        Assertions.assertThat(e.getName()).isEqualTo("National Stock Exchange");
        Assertions.assertThat(e.getCountry()).isEqualTo(ServiceIntegrationTest.COUNTRY);
        i++;
      } else {
        Assertions.assertThat(e.getExchangeCode()).isEqualTo("BSE");
        Assertions.assertThat(e.getName()).isEqualTo("Bombay Stock Exchange");
        Assertions.assertThat(e.getCountry()).isEqualTo(ServiceIntegrationTest.COUNTRY);
      }
    }
  }
}
