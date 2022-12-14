package com.thales.qrapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParseException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.thales.qrapi.dtos.ResponseObject;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.DbApiException;
import com.thales.qrapi.exceptions.NotFoundApiException;
import com.thales.qrapi.exceptions.ServerApiException;
import com.thales.qrapi.exceptions.UniqueIdentifierException;

@RestControllerAdvice
public class ApiExceptionController {

	private static final String errorInRequest = "Error detected in the request.";
	private static final String userAlreadyExists = "The provided username already exists.";
	private static final String errorReadingReq ="Exception occurred while reading the request data.";
	private static final String dataNotFound = "Data received from request was not found.";
	private static final String errorReadingDB ="Exception occurred while trying to retrieve data from the database.";
	private static final String internalServerError = "Internal Server Error occurred.";
	
	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionController.class);
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(UniqueIdentifierException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.CONFLICT));
		return new ResponseEntity<>(new ResponseObject<>(userAlreadyExists, exc.getMessage()), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(BadRequestApiException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.BAD_REQUEST));
		return new ResponseEntity<>(new ResponseObject<>(errorInRequest, exc.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(MultipartException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.BAD_REQUEST));
		return new ResponseEntity<>(new ResponseObject<>(errorInRequest, exc.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(JsonParseException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.BAD_REQUEST));
		return new ResponseEntity<>(new ResponseObject<>(errorInRequest, exc.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(HttpMessageNotReadableException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.BAD_REQUEST));
		return new ResponseEntity<>(new ResponseObject<>(errorInRequest, errorReadingReq), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(NotFoundApiException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.NOT_FOUND));
		return new ResponseEntity<>(new ResponseObject<>(dataNotFound, exc.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(DbApiException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.INTERNAL_SERVER_ERROR));
		return new ResponseEntity<>(new ResponseObject<>(internalServerError, exc.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(InvalidDataAccessResourceUsageException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.INTERNAL_SERVER_ERROR));
		return new ResponseEntity<>(new ResponseObject<>(internalServerError, errorReadingDB), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(ServerApiException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.INTERNAL_SERVER_ERROR));
		return new ResponseEntity<>(new ResponseObject<>(internalServerError, exc.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
