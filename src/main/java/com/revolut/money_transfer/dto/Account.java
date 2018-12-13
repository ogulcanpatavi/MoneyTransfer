package com.revolut.money_transfer.dto;

import java.math.BigDecimal;

public class Account {

	private String acctNum;
	private int currency;
	private BigDecimal balance;
	
	public String getAcctNum() {
		return acctNum;
	}
	public void setAcctNum(String acctNum) {
		this.acctNum = acctNum;
	}
	public int getCurrency() {
		return currency;
	}
	public void setCurrency(int currency) {
		this.currency = currency;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
	
	
	
	
	
}
