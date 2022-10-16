package com.thales.qrapi.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.repositories.interfaces.QrCodeRepository;

@Repository
public class QrCodeApiRepository implements QrCodeRepository<Long, QrCode> {

	@Autowired
	private EntityManager entityManager;
	
	// TODO: write some tests
	@Override
	public void save(QrCode qrCode) {
		
		entityManager.persist(qrCode);
	}
	
	
	// TODO: Write some tests!
	@Override
	public List<QrCode> findAll() {

	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<QrCode> query = cb.createQuery(QrCode.class);
	    Root<QrCode> root = query.from(QrCode.class);
	    query = query.select(root)
		    	.where(cb.equal(root.get("isDeleted"), false));
	    
	    return entityManager.createQuery(query).getResultList();
	}

	// TODO: write some tests!
	@Override
	public Optional<QrCode> findById(Long id) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<QrCode> query = cb.createQuery(QrCode.class);
	    Root<QrCode> root = query.from(QrCode.class);
	    query = query.select(root)
			.where(cb.and(
					cb.equal(root.get("id"), id),
					cb.equal(root.get("isDeleted"), false)
			));
	    
	    try {
			QrCode qrCode = entityManager.createQuery(query).getSingleResult();
			
			return Optional.ofNullable(qrCode);
		} catch (NoResultException exc) {
			exc.printStackTrace();
			
			return Optional.empty();
		}
	}
	
	// TODO: write some tests!
	@Override
	public void delete(QrCode qrCode) {

		qrCode.setDeleted(true);
		qrCode.setDeletedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(qrCode);
	}
}
