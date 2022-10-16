package com.thales.qrapi.exceptions;

public class DbApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public DbApiException() {}
	
	public DbApiException(String message) {
		super(message);
	}
}
