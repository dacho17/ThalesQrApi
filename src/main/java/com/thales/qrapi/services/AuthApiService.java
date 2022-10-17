package com.thales.qrapi.services;

import java.util.Optional;

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

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User Not Found with username: " + username);
		}

		return UserDetailsHelper.build(user.get());
	}
}
