package com.thales.qrapi.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationApiException extends AuthenticationException {
	private static final long serialVersionUID = 1L;
	
	public AuthenticationApiException(String message) {
		super(message);
	}
}
