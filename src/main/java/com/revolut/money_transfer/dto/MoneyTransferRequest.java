package com.revolut.money_transfer.dto;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class MoneyTransferRequest {

	private String fromAcctNum;
	private String toAcctNum;
	private Integer currency;
	private BigDecimal amount;
	private String note;
	
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
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void trim(){
		if (StringUtils.isNotBlank(fromAcctNum)) this.setFromAcctNum(fromAcctNum.trim());
		if (StringUtils.isNotBlank(toAcctNum)) this.setToAcctNum(toAcctNum.trim());
		if (StringUtils.isNotBlank(note)) this.setNote(note.trim());
	}
}
