package com.revolut.money_transfer.service;

import java.util.ArrayList;

import com.revolut.money_transfer.dao.AccountDAO;
import com.revolut.money_transfer.dao.AccountDAOImpl;
import com.revolut.money_transfer.dto.Account;

public class AdminServiceImpl implements AdminService{
	
	private AccountDAO acc = new AccountDAOImpl();
	
	@Override
	public ArrayList<Account> getListOfAccounts(){
		return acc.getListOfAccounts();
	}
}
