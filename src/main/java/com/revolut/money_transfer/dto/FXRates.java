package com.revolut.money_transfer.dto;

import java.math.BigDecimal;

public class FXRates {
	
	private int fromCurrency;
	private int toCurrency;
	private BigDecimal rate;
	
	public int getFromCurrency() {
		return fromCurrency;
	}
	public void setFromCurrency(int fromCurrency) {
		this.fromCurrency = fromCurrency;
	}
	public int getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(int toCurrency) {
		this.toCurrency = toCurrency;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	
	
}
