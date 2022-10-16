package com.thales.qrapi.exceptions;

public class ServerApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ServerApiException() {}
	
	public ServerApiException(String message) {
		super(message);
	}
}
