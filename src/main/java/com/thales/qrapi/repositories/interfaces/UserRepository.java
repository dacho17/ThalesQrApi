package com.thales.qrapi.repositories.interfaces;

import java.util.Optional;

import com.thales.qrapi.entities.User;

public interface UserRepository {
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	void save(User user);
}
