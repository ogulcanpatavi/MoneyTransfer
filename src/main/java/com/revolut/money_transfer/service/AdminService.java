package com.revolut.money_transfer.service;

import java.util.ArrayList;

import com.revolut.money_transfer.dto.Account;

public interface AdminService {

	ArrayList<Account> getListOfAccounts();

}
