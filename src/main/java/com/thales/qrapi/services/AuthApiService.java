package com.thales.qrapi.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thales.qrapi.entities.User;
import com.thales.qrapi.repositories.interfaces.UserRepository;
import com.thales.qrapi.utils.UserDetailsHelper;

@Service
public class AuthApiService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(AuthApiService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info(String.format("Attempting to find the user with [username=%s].", username));
		Optional<User> user = userRepository.findByUsername(username);
		
		if (user.isEmpty()) {
			logger.warn(String.format("User not found with [username=%s].", username));
			throw new UsernameNotFoundException("User Not Found with username: " + username);
		}

		logger.info(String.format("Successfully retrieved and about to build UserDetails object for the user with [username=%s].", username));
		return UserDetailsHelper.build(user.get());
	}
}
