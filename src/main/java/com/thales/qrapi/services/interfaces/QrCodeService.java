package com.thales.qrapi.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.DbApiException;
import com.thales.qrapi.exceptions.NotFoundApiException;
import com.thales.qrapi.exceptions.ServerApiException;

public interface QrCodeService<K, V> {
	List<V> findAll() throws DbApiException;
	V findById(K id) throws BadRequestApiException, DbApiException, NotFoundApiException;
	K deleteById(K id) throws BadRequestApiException, DbApiException, NotFoundApiException;
	K save(MultipartFile file) throws BadRequestApiException, ServerApiException, DbApiException;
}
