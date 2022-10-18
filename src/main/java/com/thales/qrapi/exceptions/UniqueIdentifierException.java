package com.thales.qrapi.exceptions;

public class UniqueIdentifierException extends IllegalStateException{
	private static final long serialVersionUID = 1L;
	
	public UniqueIdentifierException() {}
	
	public UniqueIdentifierException(String message) {
		super(message);
	}
}
