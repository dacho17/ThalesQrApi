package com.thales.qrapi.services;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thales.qrapi.dtos.auth.JwtResponse;
import com.thales.qrapi.dtos.auth.LoginRequest;
import com.thales.qrapi.dtos.auth.SignupRequest;
import com.thales.qrapi.dtos.enums.UserRole;
import com.thales.qrapi.entities.User;
import com.thales.qrapi.exceptions.DbApiException;
import com.thales.qrapi.repositories.interfaces.UserRepository;
import com.thales.qrapi.services.interfaces.UserService;
import com.thales.qrapi.utils.JwtUtils;
import com.thales.qrapi.utils.UserDetailsHelper;

@Service
public class UserApiService implements UserService {
	
	private static final String dbError = "An exception has occured in communication with the database.";
	
	private static final Logger logger = LoggerFactory.getLogger(UserApiService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public boolean existsByUsername(String username) throws DbApiException {
		logger.info(String.format("Checking if the user with [username=%s] exists in the DB.", username));
		
		try {
			return userRepository.existsByUsername(username);
		} catch(Exception exc) {
			logger.error(exc.getStackTrace().toString());
			throw new DbApiException(dbError);
		}
	}
	
	@Transactional
	public String save(SignupRequest signupRequest) throws DbApiException {
		
		UserRole roleToRegister = signupRequest.getRole();
		User user = new User(
				signupRequest.getUsername(), 
				passwordEncoder.encode(signupRequest.getPassword()),
				roleToRegister.getValue());
		
		try {
			logger.info(String.format("Storing the user with [username=%s] to the database.", signupRequest.getUsername()));
			userRepository.save(user);
			logger.info(String.format("The user with [username=%s] successfully stored to the DB.", signupRequest.getUsername()));
		} catch (Exception exc) {
			logger.error(exc.getStackTrace().toString());
			throw new DbApiException(dbError);
		}
		
		return user.getUsername();
	}
	
	public JwtResponse createJwtResponse(LoginRequest loginRequest) {
		logger.info(String.format("Creating login response for the user with [username=%s].", loginRequest.getUsername()));
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsHelper userDetails = (UserDetailsHelper) authentication.getPrincipal();		
		String role = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList()).get(0);	// NOTE: only one authority will be assigned to users
		
		logger.info(String.format("Returning the login response carrying JWT token for the user with [username=%s].", userDetails.getUsername()));
		return new JwtResponse(jwt, 
			userDetails.getId(), 
			userDetails.getUsername(),  
			role);
	}
}
