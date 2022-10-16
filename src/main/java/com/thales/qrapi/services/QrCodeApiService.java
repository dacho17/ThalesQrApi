package com.thales.qrapi.services;

import java.util.List;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thales.qrapi.dtos.QrCodeDto;
import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.DbApiException;
import com.thales.qrapi.exceptions.NotFoundApiException;
import com.thales.qrapi.mappers.QrCodeMapper;
import com.thales.qrapi.repositories.interfaces.QrCodeRepository;
import com.thales.qrapi.services.interfaces.QrCodeService;

@Service
public class QrCodeApiService implements QrCodeService<String, QrCodeDto> {

	private static final String findInDbError = "An exception has occured while finding the entry in the database.";
	private static final String notFoundInDbError = "No instance with the provided id has been found.";
	private static final String illegalArgumentsProvided = "No, or illegal arguments have been provided to the endpoint.";
	
	@Autowired
	private QrCodeRepository<Long, QrCode> qrCodeRepo;
	
	@Autowired
	private QrCodeMapper qrCodeMapper;

	// TODO: write some tests
	@Override
	@Transactional
	public List<QrCodeDto> findAll() throws DbApiException {
		try {
			List<QrCode> lst = qrCodeRepo.findAll();
			
			List<QrCodeDto> res = qrCodeMapper.mapEntListToDto(lst);
			
			return res;
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new DbApiException(findInDbError);
		}
	}

	// TODO: write some tests
	@Override
	@Transactional
	public QrCodeDto findById(String id) throws BadRequestApiException, DbApiException, NotFoundApiException {
		
		if (id == null) {
			throw new BadRequestApiException(illegalArgumentsProvided);
		}
		
		Optional<QrCode> qrCode = Optional.empty();
		try {
			long entryId = qrCodeMapper.decodeIds(id);
			qrCode = qrCodeRepo.findById(Long.valueOf(entryId));
		} catch (InvalidAttributeValueException exc) {
			exc.printStackTrace();
			throw new BadRequestApiException(exc.getMessage());
		} catch (NotFoundApiException exc) {
			exc.printStackTrace();
			throw new NotFoundApiException(exc.getMessage());
		} catch(Exception exc) {
			exc.printStackTrace();
			throw new DbApiException(findInDbError);
		}
		
		if (!qrCode.isPresent()) {
			throw new NotFoundApiException(notFoundInDbError);
		}
		
		QrCodeDto res = qrCodeMapper.mapEntToDto(qrCode.get());		
		return res;
	}
}
