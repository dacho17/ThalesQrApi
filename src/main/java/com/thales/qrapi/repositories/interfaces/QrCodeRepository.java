package com.thales.qrapi.repositories.interfaces;

import java.util.List;
import java.util.Optional;

public interface QrCodeRepository<K, V> {
	void save(V entry);
	List<V> findAll();
	Optional<V> findById(K id);
	void delete(V entry);
}
