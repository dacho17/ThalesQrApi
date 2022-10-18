package com.thales.qrapi.services.interfaces;

import com.thales.qrapi.dtos.auth.JwtResponse;
import com.thales.qrapi.dtos.auth.LoginRequest;
import com.thales.qrapi.dtos.auth.SignupRequest;
import com.thales.qrapi.exceptions.DbApiException;

public interface UserService {
	boolean existsByUsername(String username) throws DbApiException;
	String save(SignupRequest signupRequest) throws DbApiException;
	JwtResponse createJwtResponse(LoginRequest loginRequest);
}