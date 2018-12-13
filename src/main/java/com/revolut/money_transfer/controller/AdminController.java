package com.revolut.money_transfer.controller;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.revolut.money_transfer.dto.Account;
import com.revolut.money_transfer.service.AdminService;
import com.revolut.money_transfer.service.AdminServiceImpl;

@Path("admin")
public class AdminController {
	
	private final static Logger LOGGER = Logger.getLogger(AdminController.class.getName());
	private AdminService as = new AdminServiceImpl();
	
	@Path("accounts")
	@GET
	public Response getListOfAccounts() {
		LOGGER.info("Retrieving information of all acounts.");
		ArrayList<Account> list = as.getListOfAccounts();
		return Response.status(200).entity(list).build();
	}
}
