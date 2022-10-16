package com.thales.qrapi.exceptions;

import javax.management.InvalidAttributeValueException;

public class BadRequestApiException extends InvalidAttributeValueException {
	private static final long serialVersionUID = 1L;
	
	public BadRequestApiException() {}
	
	public BadRequestApiException(String message) {
		super(message);
	}
}
