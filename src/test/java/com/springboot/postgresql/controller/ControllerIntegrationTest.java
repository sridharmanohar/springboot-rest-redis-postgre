package com.springboot.postgresql.controller;

import com.springboot.postgresql.model.ExchangeList;
import com.springboot.postgresql.service.ExchangeListService;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * This class performs Controller Integration test.
 * @author sridhar
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TestController.class)
public class ControllerIntegrationTest {

  /** Service class instance for mocking. */
  @MockBean
  private ExchangeListService exchangeListService;

  /** MockMvc instance to perform mock requests and verify response. */
  @Autowired
  private MockMvc mockMvc;

  /**
   * To verify that ShowAllExchanges request returns a JSOn response.
   * @throws Exception when MockMvc faces an error
   */
  @Test
  public void whenShowAllExchanges_thenShowExchangesJSONArray() throws Exception {
    final List<ExchangeList> exchList = new ArrayList<>();
    exchList.add(new ExchangeList("NSE", "National Stock Exchange", "India"));
    exchList.add(new ExchangeList("BSE", "Bombay Stock Exchange", "India"));
    Mockito.when(exchangeListService.showAllExchanges()).thenReturn(exchList);

    mockMvc.perform(MockMvcRequestBuilders.get("/showAllExchanges"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
  }
}
