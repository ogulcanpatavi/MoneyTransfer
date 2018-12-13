package com.revolut.money_transfer.exception;

public class InsufficientBalanceException extends Exception{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8572321252685019919L;

	public InsufficientBalanceException(String message) {
		super(message);
	}

	

}
