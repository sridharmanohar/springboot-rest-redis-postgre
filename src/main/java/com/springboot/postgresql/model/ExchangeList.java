package com.springboot.postgresql.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the exchange_list database table.
 * 
 */
@Entity
@Table(name = "exchange_list")
@NamedQuery(name = "ExchangeList.findAll", query = "SELECT e FROM ExchangeList e")
public class ExchangeList implements Serializable {
  private static final long serialVersionUID = 1L;

  /** Primary key. */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_list_seq_id")
  @SequenceGenerator(name = "exchange_list_seq_id", sequenceName = "exchange_list_seq_id", 
      allocationSize = 1)
  private Integer id;

  /** Admin indicator to differentiate between active and non-active entries. */
  private Boolean active;

  /** Describes which country the exchange belongs to. */
  @JsonView(Views.Public.class)
  private String country;

  /** keeps track of the user who created the entry. */
  @Column(name = "created_by")
  private String createdBy;

  /** keeps track of the date on which this entry was created. */
  @Column(name = "created_date")
  private Timestamp createdDate;

  /** Unique and Universal exchange code for the exchange. */
  @JsonView(Views.Public.class)
  @Column(name = "exchange_code")
  private String exchangeCode;

  /** keeps track of last updated by user name. */
  @Column(name = "last_updated_by")
  private String lastUpdatedBy;

  /** Keeps track of last updated date of the field. */
  @Column(name = "last_updated_date")
  private Timestamp lastUpdatedDate;

  /** Name of the exchange. */
  @JsonView(Views.Public.class)
  private String name;

  /**
   *  bi-directional many-to-one association to StockList.
   *  List holds specific details.
   */
  @OneToMany(mappedBy = "exchangeList", fetch=FetchType.EAGER)
  @JsonIgnoreProperties("exchangeList")
  private List<StockList> stockLists;

  /**
   * Empty constructor.
   */
  public ExchangeList() {
  }

  /**
   * A 3-arg constructor used while setting JSON response for the list of
   * exchanges.
   * 
   * @param exchangeCode - The Exchange Code
   * @param name - Name of the Exchange
   * @param country - Origin country of the exchange
   */
  public ExchangeList(final String exchangeCode, final String name, final String country) {
    this.country = country;
    this.exchangeCode = exchangeCode;
    this.name = name;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Boolean getActive() {
    return this.active;
  }

  public void setActive(final Boolean active) {
    this.active = active;
  }

  public String getCountry() {
    return this.country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }

  public Timestamp getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(final Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  public String getExchangeCode() {
    return this.exchangeCode;
  }

  public void setExchangeCode(final String exchangeCode) {
    this.exchangeCode = exchangeCode;
  }

  public String getLastUpdatedBy() {
    return this.lastUpdatedBy;
  }

  public void setLastUpdatedBy(final String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public Timestamp getLastUpdatedDate() {
    return this.lastUpdatedDate;
  }

  public void setLastUpdatedDate(final Timestamp lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<StockList> getStockLists() {
    return this.stockLists;
  }

  public void setStockLists(final List<StockList> stockLists) {
    this.stockLists = stockLists;
  }

  /**
   *  To add a new stock list entry.
   *  
   * @param stockList - entity containing all stock specific details.
   * @return
   */
  public StockList addStockList(final StockList stockList) {
    getStockLists().add(stockList);
    stockList.setExchangeList(this);

    return stockList;
  }

  /**
   * To remove an entry from the stock list.
   *  
   * @param stockList -entity containing all stock specific details. 
   * @return
   */
  public StockList removeStockList(final StockList stockList) {
    getStockLists().remove(stockList);
    stockList.setExchangeList(null);

    return stockList;
  }

}