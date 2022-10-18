package com.thales.qrapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thales.qrapi.dtos.ResponseObject;
import com.thales.qrapi.dtos.auth.JwtResponse;
import com.thales.qrapi.dtos.auth.LoginRequest;
import com.thales.qrapi.dtos.auth.SignupRequest;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.UniqueIdentifierException;
import com.thales.qrapi.services.UserApiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "auth", produces = { "application/json" })
public class AuthController {

	private static final String loginSummary = "Log in to the system. If the operation is successful, the endpoint returns the JWT token used to authenticate the user in the subsequent requests.";
	private static final String signupSummary = "Create the account. If the operation is successful, the endpoint returns the username of the created user in string format.";
	
	private static final String loginSuccess = "Login successful and JWT generated.";
	private static final String signupSuccess = "User registered successfully.";
	
	private static final String errorInRequest = "Error detected in the request.";
	private static final String unauthenticatedUser = "Login failed for this user.";
	private static final String userAlreadyExists = "The provided username already exists.";
	private static final String internalServerError = "Internal Server Error occurred.";
	
	private static final String httpCreated = "201";
	private static final String httpBadReq = "400";
	private static final String httpNotAuthenicated = "401";
	private static final String httpConflict = "409";
	private static final String httpServerError = "500";
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private UserApiService userService;
	
	@Operation(summary = loginSummary)
	@ApiResponses(value = {
		@ApiResponse(responseCode = httpCreated, description = loginSuccess, content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)),}),
		@ApiResponse(responseCode = httpBadReq, description = errorInRequest, content = @Content),
		@ApiResponse(responseCode = httpNotAuthenicated, description = unauthenticatedUser, content = @Content),
		@ApiResponse(responseCode = httpServerError, description = internalServerError, content = @Content) })
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("/login")
	public ResponseObject<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
		logger.info("POST /login endpoint accessed.");
		
		JwtResponse jwtRes = userService.createJwtResponse(loginRequest);
		
		logger.info(loginSuccess);
		return new ResponseObject<>(loginSuccess, jwtRes);
	} 
	
	@Operation(summary = signupSummary)
	@ApiResponses(value = {
		@ApiResponse(responseCode = httpCreated, description = signupSuccess, content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)),}),
		@ApiResponse(responseCode = httpBadReq, description = errorInRequest, content = @Content),
		@ApiResponse(responseCode = httpConflict, description = userAlreadyExists, content = @Content),
		@ApiResponse(responseCode = httpServerError, description = internalServerError, content = @Content) })
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("/signup")
	public ResponseObject<String> signup(@RequestBody SignupRequest signupRequest) throws Exception {
		logger.info("POST /signup endpoint accessed.");
		
		if (userService.existsByUsername(signupRequest.getUsername())) {
			logger.error(String.format("User with [username=%s] already exists in the system.", signupRequest.getUsername()));
			throw new UniqueIdentifierException(userAlreadyExists);
		}
		
		try {
			String newUsername = userService.save(signupRequest);
			
			logger.info(signupSuccess);
			return new ResponseObject<>(signupSuccess, newUsername);
		} catch (Exception exc) {
			logger.error(exc.getMessage());
			throw new BadRequestApiException(errorInRequest);
		}
	}
}
