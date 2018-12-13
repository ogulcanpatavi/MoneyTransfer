package com.revolut.money_transfer.dto;

public class CachedAccounts {
	
	private Account fromAcct;
	private Account toAcct;
	
	public Account getFromAcct() {
		return fromAcct;
	}
	public void setFromAcct(Account fromAcct) {
		this.fromAcct = fromAcct;
	}
	public Account getToAcct() {
		return toAcct;
	}
	public void setToAcct(Account toAcct) {
		this.toAcct = toAcct;
	}

}
