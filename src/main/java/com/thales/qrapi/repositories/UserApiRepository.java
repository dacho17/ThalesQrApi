package com.thales.qrapi.repositories;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.thales.qrapi.entities.User;
import com.thales.qrapi.repositories.interfaces.UserRepository;

@Repository
public class UserApiRepository implements UserRepository {
	
	@Autowired
	private EntityManager entityManager;
	
	public Optional<User> findByUsername(String username) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<User> query = cb.createQuery(User.class);
	    Root<User> root = query.from(User.class);
	    query = query.select(root)
			.where(cb.equal(root.get("username"), username));
	    
	    try {
	    	User res = entityManager.createQuery(query).getSingleResult();
			
			return Optional.ofNullable(res);
		} catch (NoResultException exc) {	// exception occurs because getSingleResult throws it if not entries have been found
			return Optional.empty();
		}
	}
	
	public boolean existsByUsername(String username) {
		
		return findByUsername(username).isPresent();
	}
	
	public void save(User user) {
		
		entityManager.persist(user);
	}
}
