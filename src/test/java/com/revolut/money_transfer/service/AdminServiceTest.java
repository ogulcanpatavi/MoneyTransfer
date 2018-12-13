package com.revolut.money_transfer.service;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revolut.money_transfer.dao.AccountDAO;
import com.revolut.money_transfer.dto.Account;


public class AdminServiceTest {
	
	@InjectMocks
	private AdminServiceImpl adminServiceImpl;
	
	@Mock
	private AccountDAO acctDao;	

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void checkSizeOfAccounts() {
		ArrayList<Account> a = new ArrayList<>();
		a.add(new Account());
		a.add(new Account());
		Mockito.when(acctDao.getListOfAccounts()).thenReturn(a);
		Assert.assertEquals(adminServiceImpl.getListOfAccounts().size(), 2);
	}
	
	
}
