package com.thales.qrapi.exceptions;

import javax.management.InstanceNotFoundException;

public class NotFoundApiException extends InstanceNotFoundException{
	private static final long serialVersionUID = 1L;
	
	public NotFoundApiException() {}
	
	public NotFoundApiException(String message) {
		super(message);
	}
}
