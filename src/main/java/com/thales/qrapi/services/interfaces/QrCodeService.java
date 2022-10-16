package com.thales.qrapi.services.interfaces;

import java.util.List;

import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.DbApiException;
import com.thales.qrapi.exceptions.NotFoundApiException;

public interface QrCodeService<K, V> {
	List<V> findAll() throws DbApiException;
	V findById(K id) throws BadRequestApiException, DbApiException, NotFoundApiException;
}
