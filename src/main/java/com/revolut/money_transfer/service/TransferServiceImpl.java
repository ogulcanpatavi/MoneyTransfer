package com.revolut.money_transfer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.revolut.money_transfer.dao.AccountDAO;
import com.revolut.money_transfer.dao.AccountDAOImpl;
import com.revolut.money_transfer.dao.FxRatesDAO;
import com.revolut.money_transfer.dao.FxRatesDAOImpl;
import com.revolut.money_transfer.dto.Account;
import com.revolut.money_transfer.dto.CachedAccounts;
import com.revolut.money_transfer.dto.FXRates;
import com.revolut.money_transfer.dto.MoneyTransferRequest;
import com.revolut.money_transfer.dto.MoneyTransferResponse;
import com.revolut.money_transfer.exception.DataNotAvailableException;
import com.revolut.money_transfer.exception.InsufficientBalanceException;
import com.revolut.money_transfer.exception.TransferCompletionException;
import com.revolut.money_transfer.utilities.Cached;

public class TransferServiceImpl implements TransferService {

	private AccountDAO acc = new AccountDAOImpl();
	private FxRatesDAO fxr = new FxRatesDAOImpl();
	private CachedAccounts cachedAcct = new CachedAccounts();
	private final static Logger LOGGER = Logger.getLogger(TransferServiceImpl.class.getName());

	@Override
	public ArrayList<String> validateTransferRequest(MoneyTransferRequest mtrequest) {
		LOGGER.info("Validating the request body for Money Transfer API...");
		ArrayList<String> errorMessages = new ArrayList<>();
		if (StringUtils.isBlank(mtrequest.getFromAcctNum())) {
			errorMessages.add("fromAcctNum field cannot be null.");
		} else {
			Account fromAcct = acc.getAccountByNum(mtrequest.getFromAcctNum());
			if (fromAcct == null) {
				errorMessages.add("No Account found: " + mtrequest.getFromAcctNum());
			} else {
				cachedAcct.setFromAcct(fromAcct);
			}
		}
		if (StringUtils.isBlank(mtrequest.getToAcctNum())) {
			errorMessages.add("toAcctNum field cannot be null.");
		} else {
			Account toAcct = acc.getAccountByNum(mtrequest.getToAcctNum());
			if (toAcct == null) {
				errorMessages.add("No Account found: " + mtrequest.getToAcctNum());
			} else {
				if (toAcct.getCurrency() != mtrequest.getCurrency()) {
					errorMessages.add("The account you are transferring funds to only accepts "
							+ Cached.getCurrency(toAcct.getCurrency()) + "(" + toAcct.getCurrency() + ").");
				} else {
					cachedAcct.setToAcct(toAcct);
				}
			}
		}
		if (mtrequest.getCurrency() == null || mtrequest.getCurrency() <= 0) {
			errorMessages.add("currency field cannot be null and/or less than 0.");
		} else {
			if (!Cached.isCurrency(mtrequest.getCurrency())) {
				errorMessages.add("currency as " + mtrequest.getCurrency() + " is invalid. Available Currencies: "
						+ Cached.currencyList());
			}
		}
		if (mtrequest.getAmount() == null || (mtrequest.getAmount().compareTo(BigDecimal.ZERO)) <= 0) {
			errorMessages.add("amount field cannot be null and/or less than 0.");
		}
		return errorMessages;
	}

	@Override
	public MoneyTransferResponse processTransfer(MoneyTransferRequest mtrequest) throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException {
		LOGGER.info("Processing the payment...");
		BigDecimal fromAmount;
		Account cachedToAcct = cachedAcct.getToAcct();
		Account cachedFromAcct = cachedAcct.getFromAcct();
		if (cachedToAcct.getCurrency() != cachedFromAcct.getCurrency()) {
			FXRates fx = fxr.getRate(cachedToAcct.getCurrency(), cachedFromAcct.getCurrency());
			if (fx == null)
				throw new DataNotAvailableException("No Rate Available at this moment. From: "
						+ cachedFromAcct.getCurrency() + " To: " + cachedToAcct.getCurrency());
			BigDecimal rate = fx.getRate();
			fromAmount = mtrequest.getAmount().multiply(rate);
		} else {
			fromAmount = mtrequest.getAmount();
		}
		if (fromAmount.compareTo(cachedFromAcct.getBalance()) > 0)
			throw new InsufficientBalanceException("Not Enough Fund Available for this transaction. Available Balance: "
					+ cachedFromAcct.getBalance() + " " + Cached.getCurrency(cachedFromAcct.getCurrency()));
		BigDecimal fromBalance = cachedFromAcct.getBalance().subtract(fromAmount);
		BigDecimal toBalance = cachedToAcct.getBalance().add(mtrequest.getAmount());
		
		boolean processed = acc.updateBalance(fromBalance, mtrequest.getFromAcctNum(), toBalance, mtrequest.getToAcctNum());
		if (!processed) throw new TransferCompletionException("We are not able to transfer the funds. Please contact the Customer Service");
		MoneyTransferResponse response = new MoneyTransferResponse();
		response.setFromAcctNum(mtrequest.getFromAcctNum());
		response.setFromAmount(fromAmount.negate());
		response.setFromCurrency(cachedFromAcct.getCurrency());
		response.setToAcctNum(mtrequest.getToAcctNum());
		response.setToAmount(mtrequest.getAmount());
		response.setToCurrency(cachedToAcct.getCurrency());
		return response;

	}
}
