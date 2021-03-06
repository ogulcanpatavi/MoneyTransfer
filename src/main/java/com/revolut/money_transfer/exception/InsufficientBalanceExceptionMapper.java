package com.revolut.money_transfer.exception;

import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.revolut.money_transfer.dto.ErrorVO;

@Provider
public class InsufficientBalanceExceptionMapper implements ExceptionMapper<InsufficientBalanceException>{

	@Override
	public Response toResponse(InsufficientBalanceException arg0) {
		ErrorVO err = new ErrorVO();
		ArrayList<String> ar = new ArrayList<>();
		ar.add(arg0.getMessage());
		err.setErrorMessages(ar);
		return Response.status(400).entity(err).type(MediaType.APPLICATION_JSON).build();
	}

}
