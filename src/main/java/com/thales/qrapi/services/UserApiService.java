package com.thales.qrapi.services;

import java.util.stream.Collectors;

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
import com.thales.qrapi.repositories.interfaces.UserRepository;
import com.thales.qrapi.utils.JwtUtils;
import com.thales.qrapi.utils.UserDetailsHelper;

@Service
public class UserApiService {	// implements UserDetailsService
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	@Transactional
	public String save(SignupRequest signupRequest) {
		UserRole roleToRegister = signupRequest.getRole();

		User user = new User(
			signupRequest.getUsername(), 
			passwordEncoder.encode(signupRequest.getPassword()),
			roleToRegister.getValue());

		userRepository.save(user);
		
		return user.getUsername();
	}
	
	public JwtResponse createJwtResponse(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsHelper userDetails = (UserDetailsHelper) authentication.getPrincipal();		
		String role = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList()).get(0);	// NOTE: only one authority will be assigned to users
		
		return new JwtResponse(jwt, 
			userDetails.getId(), 
			userDetails.getUsername(),  
			role);
	}
}
