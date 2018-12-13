package com.revolut.money_transfer.dto;

import java.util.ArrayList;

public class ErrorVO {
	private ArrayList<String> errorMessages;

	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(ArrayList<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	
}
