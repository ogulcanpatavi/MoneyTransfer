package com.revolut.money_transfer.utilities;

public class Queries {

	public static final String GET_ACCOUNTS = "select * from Account";
	public static final String GET_ACCOUNT_BY_ID = "Select * from Account a where a.AcctNum = ?";
	public static final String UPDATE_BALANCE = "UPDATE Account a SET a.Balance = ? WHERE a.AcctNum = ?";
	public static final String GET_FX_RATE = "Select f.Rate from FxRates f where f.FromCurrency = ? and ToCurrency = ?";
}
