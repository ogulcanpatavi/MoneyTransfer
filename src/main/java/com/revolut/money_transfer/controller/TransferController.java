package com.revolut.money_transfer.controller;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revolut.money_transfer.dto.ErrorVO;
import com.revolut.money_transfer.dto.MoneyTransferRequest;
import com.revolut.money_transfer.dto.MoneyTransferResponse;
import com.revolut.money_transfer.exception.DataNotAvailableException;
import com.revolut.money_transfer.exception.InsufficientBalanceException;
import com.revolut.money_transfer.exception.TransferCompletionException;
import com.revolut.money_transfer.service.TransferService;
import com.revolut.money_transfer.service.TransferServiceImpl;

@Path("payment")
public class TransferController {

	private final static Logger LOGGER = Logger.getLogger(TransferController.class.getName());
	private TransferService ts = new TransferServiceImpl();
	
	@Path("transfer")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response transfer(MoneyTransferRequest request) throws DataNotAvailableException, InsufficientBalanceException, TransferCompletionException{
		LOGGER.info("Money Transfer Request is received.");
		request.trim();
		ArrayList<String> errorMessages = ts.validateTransferRequest(request);
		if (errorMessages.size() > 0) {
			ErrorVO error = new ErrorVO();
			error.setErrorMessages(errorMessages);
			return Response.status(400).entity(error).build();
		}
		MoneyTransferResponse response = ts.processTransfer(request);
		LOGGER.info("Money Transfer is completed.");
		return Response.status(201).entity(response).build();
	}
}
