package com.mm.rest.pricechecker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class StockQuote {

	@Id
	@NotNull
	@JsonProperty("Symbol")
	@Column(name="symbol", unique=true, nullable=false)
    private String symbol;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@NotNull
	@JsonProperty("Name")
	@Column(name="stockName", unique=true, nullable=false)
	private String stockName;

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}


	@NotNull
	@JsonProperty("LastPrice")
	@Column(name="lastPrice", nullable=false)
	private Double lastPrice;

	public Double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
	}

	@NotNull
	@JsonProperty("Change")
	@Column(name="priceChange", nullable=false)
	private Double priceChange;
	
	public Double getPriceChange() {
		return priceChange;
	}

	public void setPriceChange(Double priceChange) {
		this.priceChange = priceChange;
	}

}
