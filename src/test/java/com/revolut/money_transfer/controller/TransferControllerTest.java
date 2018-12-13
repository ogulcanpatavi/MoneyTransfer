package com.revolut.money_transfer.controller;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revolut.money_transfer.dto.MoneyTransferRequest;
import com.revolut.money_transfer.exception.DataNotAvailableException;
import com.revolut.money_transfer.exception.InsufficientBalanceException;
import com.revolut.money_transfer.exception.TransferCompletionException;
import com.revolut.money_transfer.service.TransferService;


public class TransferControllerTest {

	@InjectMocks
	private TransferController transferController;
	
	@Mock
	private TransferService ts;
	
	@Mock
	private MoneyTransferRequest mtrequest;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void checkWithErrorMessages() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException{
		Mockito.when(mtrequest.getAmount()).thenReturn(new BigDecimal(1234));
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000001");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("000002");
		ArrayList<String> a = new ArrayList<>();
		a.add("something");
		Mockito.when(ts.validateTransferRequest(mtrequest)).thenReturn(a);
		Assert.assertEquals(transferController.transfer(mtrequest).getStatus(), 400);
	}
	
	@Test
	public void checkSuccessful() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException{
		Mockito.when(mtrequest.getAmount()).thenReturn(new BigDecimal(1234));
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000001");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("000002");
		Mockito.when(ts.validateTransferRequest(mtrequest)).thenReturn(new ArrayList<String>());
		Assert.assertEquals(transferController.transfer(mtrequest).getStatus(), 201);
	}
}
