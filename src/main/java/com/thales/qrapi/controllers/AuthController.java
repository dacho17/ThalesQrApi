package com.thales.qrapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thales.qrapi.dtos.ResponseObject;
import com.thales.qrapi.dtos.auth.JwtResponse;
import com.thales.qrapi.dtos.auth.LoginRequest;
import com.thales.qrapi.dtos.auth.SignupRequest;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.services.UserApiService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private UserApiService userService;
	
	@PostMapping("/login")
	public ResponseObject<JwtResponse> login(@RequestBody LoginRequest loginRequest) {

		JwtResponse jwtRes = userService.createJwtResponse(loginRequest);
		
		return new ResponseObject<>("Jwt generated", jwtRes);
	} 
	
	@PostMapping("/signup")
	public ResponseObject<String> signup(@RequestBody SignupRequest signupRequest) throws Exception {	// what does @Valid annotation stand for?

		if (userService.existsByUsername(signupRequest.getUsername())) {
			return new ResponseObject<>("Username taken", null);
		}
		
		try {
			String newUsername = userService.save(signupRequest);
			
			return new ResponseObject<>("User registered successfully!", newUsername);
		} catch (Exception exc) {
			throw new BadRequestApiException("Parameters provided in the requests are invalid");
		}
	}
}
