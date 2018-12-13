package com.revolut.money_transfer.service;

import java.util.ArrayList;

import com.revolut.money_transfer.dto.MoneyTransferRequest;
import com.revolut.money_transfer.dto.MoneyTransferResponse;
import com.revolut.money_transfer.exception.DataNotAvailableException;
import com.revolut.money_transfer.exception.InsufficientBalanceException;
import com.revolut.money_transfer.exception.TransferCompletionException;

public interface TransferService {

	ArrayList<String> validateTransferRequest(MoneyTransferRequest mtrequest);

	MoneyTransferResponse processTransfer(MoneyTransferRequest mtrequest) throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException;
	
}
