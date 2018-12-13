package com.revolut.money_transfer.dao;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.revolut.money_transfer.dto.Account;

public interface AccountDAO {

	ArrayList<Account> getListOfAccounts();

	Account getAccountByNum(String num);

	boolean updateBalance(BigDecimal fromBalance, String fromAcctNum, BigDecimal toBalance, String toAcctNum);

}
