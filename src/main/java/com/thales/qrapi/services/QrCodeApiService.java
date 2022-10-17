package com.thales.qrapi.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thales.qrapi.dtos.qrcode.QrCodeDto;
import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.DbApiException;
import com.thales.qrapi.exceptions.NotFoundApiException;
import com.thales.qrapi.exceptions.ServerApiException;
import com.thales.qrapi.mappers.QrCodeMapper;
import com.thales.qrapi.repositories.interfaces.QrCodeRepository;
import com.thales.qrapi.services.interfaces.QrCodeService;
import com.thales.qrapi.utils.QrCodeHelper;

@Service
public class QrCodeApiService implements QrCodeService<String, QrCodeDto> {

	private static final String dbError = "An exception has occured in communication with the database.";
	private static final String notFoundInDbError = "No instance with the provided id has been found.";
	private static final String illegalArgumentsProvided = "No, or illegal arguments have been provided to the endpoint.";
	private static final String errorReadingFile = "An error has occured while processing the uploaded file.";
	
	@Autowired
	private QrCodeRepository<Long, QrCode> qrCodeRepo;
	
	@Autowired
	private QrCodeMapper qrCodeMapper;
	
	@Autowired
	private QrCodeHelper qrCodeHelper;
	
	// TODO: write some tests
	@Override
	@Transactional
	public String save(MultipartFile file) throws BadRequestApiException, ServerApiException, DbApiException {
		try {
			byte[] bytes = file.getBytes();
			QrCode newQrCode = qrCodeHelper.generateNewQrCode(bytes, file);

			qrCodeRepo.save(newQrCode);
			return qrCodeMapper.encodeId(newQrCode.getId());
		} catch(IOException exc) {
			exc.printStackTrace();
			throw new BadRequestApiException(errorReadingFile);
		} catch (ServerApiException exc) {
			exc.printStackTrace();
			throw new ServerApiException(exc.getMessage());
		} catch (BadRequestApiException exc) {
			exc.printStackTrace();
			throw new BadRequestApiException(exc.getMessage());
		} catch(Exception exc) {
			exc.printStackTrace();
			throw new DbApiException(dbError);
		}
	}

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
			throw new DbApiException(dbError);
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
		} catch (BadRequestApiException exc) {
			exc.printStackTrace();
			throw new BadRequestApiException(exc.getMessage());
		} catch (NotFoundApiException exc) {
			exc.printStackTrace();
			throw new NotFoundApiException(exc.getMessage());
		} catch(Exception exc) {
			exc.printStackTrace();
			throw new DbApiException(dbError);
		}
		
		if (!qrCode.isPresent()) {
			throw new NotFoundApiException(notFoundInDbError);
		}
		
		QrCodeDto res = qrCodeMapper.mapEntToDto(qrCode.get());		
		return res;
	}

	// TODO: write some tests
	@Override
	@Transactional
	public String deleteById(String id) throws BadRequestApiException, DbApiException, NotFoundApiException {
		
		if (id == null) {
			throw new BadRequestApiException(illegalArgumentsProvided);
		}
		
		Optional<QrCode> qrCode = Optional.empty();
		try {
			long entryId = qrCodeMapper.decodeIds(id);
			qrCode = qrCodeRepo.findById(Long.valueOf(entryId));
			
			if (!qrCode.isPresent()) {
				throw new NotFoundApiException(notFoundInDbError);
			}
			
			qrCodeRepo.delete(qrCode.get());
		} catch (BadRequestApiException exc) {
			exc.printStackTrace();
			throw new BadRequestApiException(exc.getMessage());
		} catch (NotFoundApiException exc) {
			exc.printStackTrace();
			throw new NotFoundApiException(exc.getMessage());
		} catch(Exception exc) {
			exc.printStackTrace();
			throw new DbApiException(dbError);
		}
		
		return id;
	}
}
