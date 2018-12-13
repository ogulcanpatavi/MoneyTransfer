package com.revolut.money_transfer.dto;

import java.math.BigDecimal;

public class MoneyTransferResponse {

	private String fromAcctNum;
	private String toAcctNum;
	private int fromCurrency;
	private int toCurrency;
	private BigDecimal fromAmount;
	private BigDecimal toAmount;
	
	public String getFromAcctNum() {
		return fromAcctNum;
	}
	public void setFromAcctNum(String fromAcctNum) {
		this.fromAcctNum = fromAcctNum;
	}
	public String getToAcctNum() {
		return toAcctNum;
	}
	public void setToAcctNum(String toAcctNum) {
		this.toAcctNum = toAcctNum;
	}
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
	public BigDecimal getFromAmount() {
		return fromAmount;
	}
	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}
	public BigDecimal getToAmount() {
		return toAmount;
	}
	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}
}
