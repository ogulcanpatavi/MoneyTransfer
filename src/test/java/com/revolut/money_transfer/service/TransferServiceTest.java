package com.revolut.money_transfer.service;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revolut.money_transfer.dao.AccountDAO;
import com.revolut.money_transfer.dao.FxRatesDAO;
import com.revolut.money_transfer.dto.Account;
import com.revolut.money_transfer.dto.CachedAccounts;
import com.revolut.money_transfer.dto.FXRates;
import com.revolut.money_transfer.dto.MoneyTransferRequest;
import com.revolut.money_transfer.exception.DataNotAvailableException;
import com.revolut.money_transfer.exception.InsufficientBalanceException;
import com.revolut.money_transfer.exception.TransferCompletionException;
import com.revolut.money_transfer.utilities.Cached;

import org.junit.Assert;

public class TransferServiceTest {

	@InjectMocks
	private TransferServiceImpl ts;

	@Mock
	private AccountDAO acctDao;
	
	@Mock
	private FxRatesDAO fxDao;
	
	@Mock
	private FXRates fxrates;
	
	@Mock
	private Account fromAcct;
	
	@Mock
	private Account toAcct;
	
	@Mock
	private CachedAccounts cached;

	@Mock
	private MoneyTransferRequest mtrequest;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void checkIfGivenCorrectly() {
		Mockito.when(mtrequest.getAmount()).thenReturn(new BigDecimal(1234));
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000001");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("000002");
		Mockito.when(acctDao.getAccountByNum("000001")).thenReturn(fromAcct);
		Mockito.when(acctDao.getAccountByNum("000002")).thenReturn(toAcct);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Assert.assertEquals(ts.validateTransferRequest(mtrequest).size(), 0);
	}

	@Test
	public void checkIfNoAmount() {
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000001");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("000002");
		Mockito.when(acctDao.getAccountByNum("000001")).thenReturn(fromAcct);
		Mockito.when(acctDao.getAccountByNum("000002")).thenReturn(toAcct);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Assert.assertTrue(
				ts.validateTransferRequest(mtrequest).contains("amount field cannot be null and/or less than 0."));
	}

	@Test
	public void checkIfNoFromAcctNum() {
		Mockito.when(mtrequest.getAmount()).thenReturn(new BigDecimal(1234));
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("000002");
		Mockito.when(acctDao.getAccountByNum("000002")).thenReturn(toAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Assert.assertTrue(ts.validateTransferRequest(mtrequest).contains("fromAcctNum field cannot be null."));
	}

	@Test
	public void checkIfNoToAcctNum() {
		Mockito.when(mtrequest.getAmount()).thenReturn(new BigDecimal(1234));
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000002");
		Mockito.when(acctDao.getAccountByNum("000002")).thenReturn(toAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Assert.assertTrue(ts.validateTransferRequest(mtrequest).contains("toAcctNum field cannot be null."));
	}

	@Test
	public void checkIfInvalidCurrency() {
		Mockito.when(mtrequest.getAmount()).thenReturn(new BigDecimal(1234));
		Mockito.when(mtrequest.getCurrency()).thenReturn(841);
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("000001");
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000002");
		Mockito.when(acctDao.getAccountByNum("000001")).thenReturn(fromAcct);
		Mockito.when(acctDao.getAccountByNum("000002")).thenReturn(toAcct);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(toAcct.getCurrency()).thenReturn(841);
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Assert.assertTrue(ts.validateTransferRequest(mtrequest).contains("currency as " + mtrequest.getCurrency()
				+ " is invalid. Available Currencies: " + Cached.currencyList()));
	}

	@Test
	public void checkIfNegativeBalance() {
		Mockito.when(mtrequest.getAmount()).thenReturn(new BigDecimal(1234).negate());
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000001");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("000002");
		Mockito.when(acctDao.getAccountByNum("000001")).thenReturn(fromAcct);
		Mockito.when(acctDao.getAccountByNum("000002")).thenReturn(toAcct);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Assert.assertTrue(ts.validateTransferRequest(mtrequest).contains("amount field cannot be null and/or less than 0."));
	}

	@Test
	public void checkIfNoAcctFound() {
		Mockito.when(mtrequest.getAmount()).thenReturn(new BigDecimal(1234).negate());
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("000002");
		Mockito.when(acctDao.getAccountByNum("0000012")).thenReturn(null);
		Mockito.when(acctDao.getAccountByNum("000002")).thenReturn(toAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Assert.assertTrue(ts.validateTransferRequest(mtrequest).contains("No Account found: 0000012"));
	}

	@Test
	public void processTransferSuccessCheckFromAmount() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(12);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(13));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(40));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(true);
		Assert.assertEquals(ts.processTransfer(mtrequest).getFromAmount(), amount.negate());
	}
	
	@Test
	public void processTransferSuccessCheckToAmount() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(12);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(13));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(40));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(true);
		Assert.assertEquals(ts.processTransfer(mtrequest).getToAmount(), amount);
	}
	
	@Test
	public void processTransferSuccessCheckToAcct() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(12);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(13));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(40));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(true);
		Assert.assertEquals(ts.processTransfer(mtrequest).getToAcctNum(),"0000012");
	}
	
	@Test
	public void processTransferSuccessCheckFromAcct() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(12);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(13));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(40));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(true);
		Assert.assertEquals(ts.processTransfer(mtrequest).getFromAcctNum(),"000011");
	}
	
	@Test(expected = TransferCompletionException.class) 
	public void processTransferNotProcessed() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(12);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(13));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(40));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(false);
		ts.processTransfer(mtrequest);
	}
	
	@Test(expected = InsufficientBalanceException.class) 
	public void processTransferInsufficientFund() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(14);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(13));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(40));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(false);
		ts.processTransfer(mtrequest);
	}
	
	@Test(expected = DataNotAvailableException.class) 
	public void processTransferDataNotAvailableException() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(14);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(949);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fxDao.getRate(949, 840)).thenReturn(null);
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(13));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(40));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(false);
		ts.processTransfer(mtrequest);
	}
	
	@Test
	public void processTransferDifferentCurrency() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(14);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(949);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fxDao.getRate(949, 840)).thenReturn(fxrates);
		Mockito.when(fxrates.getRate()).thenReturn(new BigDecimal(2));
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(33));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(40));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(true);
		Assert.assertEquals(ts.processTransfer(mtrequest).getFromAmount(), new BigDecimal(28).negate());
	}
	
	@Test(expected=InsufficientBalanceException.class)
	public void processTransferDifferentCurrencyNoFund() throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		BigDecimal amount = new BigDecimal(14);
		Mockito.when(mtrequest.getAmount()).thenReturn(amount);
		Mockito.when(mtrequest.getCurrency()).thenReturn(840);
		Mockito.when(mtrequest.getFromAcctNum()).thenReturn("000011");
		Mockito.when(mtrequest.getToAcctNum()).thenReturn("0000012");
		Mockito.when(mtrequest.getNote()).thenReturn("coffee");
		Mockito.when(cached.getToAcct()).thenReturn(toAcct);
		Mockito.when(cached.getFromAcct()).thenReturn(fromAcct);
		Mockito.when(toAcct.getCurrency()).thenReturn(949);
		Mockito.when(fromAcct.getCurrency()).thenReturn(840);
		Mockito.when(fxDao.getRate(949, 840)).thenReturn(fxrates);
		Mockito.when(fxrates.getRate()).thenReturn(new BigDecimal(2));
		Mockito.when(fromAcct.getBalance()).thenReturn(new BigDecimal(23));
		Mockito.when(toAcct.getBalance()).thenReturn(new BigDecimal(20));
		Mockito.when(acctDao.updateBalance(Mockito.any(BigDecimal.class), Mockito.any(String.class), Mockito.any(BigDecimal.class), Mockito.any(String.class))).thenReturn(true);
		ts.processTransfer(mtrequest);
	}
}
