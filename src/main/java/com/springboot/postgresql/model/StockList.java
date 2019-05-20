package com.springboot.postgresql.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * The persistent class for the stock_list database table.
 * 
 */
@Entity
@Table(name = "stock_list")
@NamedQuery(name = "StockList.findAll", query = "SELECT s FROM StockList s")
public class StockList implements Serializable {
  private static final long serialVersionUID = 1L;

  /** Primary key. */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_list_seq_id")
  @SequenceGenerator(name = "stock_list_seq_id", sequenceName = "stock_list_seq_id", 
      allocationSize = 1)
  private Integer id;

  /** Active/Inactive - admin indicator. */
  private Boolean active;

  /** Company name of the stock. */
  @Column(name = "company_name")
  @JsonView(Views.StockView.class)
  private String companyName;

  /** Describes the index to which the stock belongs to. */
  @Column(name = "sectoral_index")
  @JsonView(Views.StockView.class)
  private String sectoralIndex;

  /** Indicates whether the stock is listed or unlisted. */
  @JsonView(Views.StockView.class)
  private String status;

  /** Exchange specific stock quote/code. */
  @Column(name = "stock_code")
  @JsonView(Views.StockView.class)
  private String stockCode;

  /** Date on which the stock was listed on the exchange. */
  @Temporal(TemporalType.DATE)
  @Column(name = "stock_listed_date")
  @JsonView(Views.StockView.class)
  private Date stockListedDate;

  /** bi-directional many-to-one association to ExchangeList. */
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name = "exchange_code_id")
  private ExchangeList exchangeList;

  /**
   * Default const.
   */
  public StockList() {
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

  public String getCompanyName() {
    return this.companyName;
  }

  public void setCompanyName(final String companyName) {
    this.companyName = companyName;
  }

  public String getSectoralIndex() {
    return this.sectoralIndex;
  }

  public void setSectoralIndex(final String sectoralIndex) {
    this.sectoralIndex = sectoralIndex;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public String getStockCode() {
    return this.stockCode;
  }

  public void setStockCode(final String stockCode) {
    this.stockCode = stockCode;
  }

  public Date getStockListedDate() {
    return this.stockListedDate;
  }

  public void setStockListedDate(final Date stockListedDate) {
    this.stockListedDate = stockListedDate;
  }

  public ExchangeList getExchangeList() {
    return this.exchangeList;
  }

  public void setExchangeList(final ExchangeList exchangeList) {
    this.exchangeList = exchangeList;
  }

}