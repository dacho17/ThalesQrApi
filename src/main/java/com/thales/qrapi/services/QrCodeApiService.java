package com.thales.qrapi.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	private static final String errorReadingFile = "An error has occured while processing the uploaded file.";
	
	private static final Logger logger = LoggerFactory.getLogger(QrCodeApiService.class);
	
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
			
			QrCode newQrCode = qrCodeHelper.generateNewQrCode(bytes, file.getOriginalFilename());
			setCreatedByForNewQrCode(newQrCode);
			
			logger.info(String.format("About to store the newly generated qrCode with [filename=%s].", newQrCode.getFileName()));
			qrCodeRepo.save(newQrCode);
			logger.info(String.format("QrCode with [filename=%s], and [id=%d] has been stored.", newQrCode.getFileName(), newQrCode.getId()));
			return qrCodeMapper.encodeId(newQrCode.getId());
		} catch(IOException exc) {			// NOTE: specific errors/exceptions will be logged on lower levels
			throw new BadRequestApiException(errorReadingFile);
		} catch (ServerApiException exc) {
			throw new ServerApiException(exc.getMessage());
		} catch (BadRequestApiException exc) {
			throw new BadRequestApiException(exc.getMessage());
		} catch(Exception exc) {
			logger.error(exc.getStackTrace().toString());
			throw new DbApiException(dbError);
		}
	}

	// TODO: write some tests
	@Override
	@Transactional
	public List<QrCodeDto> findAll() throws DbApiException {
		logger.info("About to fetch QrCodes from the DB.");
		
		try {
			List<QrCode> lst = qrCodeRepo.findAll();
			
			List<QrCodeDto> res = qrCodeMapper.mapEntListToDto(lst);
			
			logger.info("QrCodes have successfully been fetched from the DB.");
			return res;
		} catch (Exception exc) {
			logger.error(exc.getStackTrace().toString());
			throw new DbApiException(dbError);
		}
	}

	// TODO: write some tests
	@Override
	@Transactional
	public QrCodeDto findById(String id) throws BadRequestApiException, DbApiException, NotFoundApiException {
		
		long entryId;
		Optional<QrCode> qrCode = Optional.empty();
		try {
			entryId = qrCodeMapper.decodeIds(id);

			logger.info(String.format("About to fetch QrCode with [id=%d] from the DB.", Long.valueOf(entryId)));
			qrCode = qrCodeRepo.findById(Long.valueOf(entryId));
		} catch (BadRequestApiException exc) {
			throw new BadRequestApiException(exc.getMessage());
		} catch (NotFoundApiException exc) {
			throw new NotFoundApiException(exc.getMessage());
		} catch(Exception exc) {
			logger.error(exc.getStackTrace().toString());
			throw new DbApiException(dbError);
		}
		
		if (!qrCode.isPresent()) {
			logger.warn(String.format("QrCode with [id=%d] has not been found in the DB.", Long.valueOf(entryId)));
			throw new NotFoundApiException(notFoundInDbError);
		}
		
		QrCode fetchedQrCode = qrCode.get();
		
		logger.info(String.format("QrCode with [filename=%s], and [id=%d] has successfully been fetched.", fetchedQrCode.getFileName(), fetchedQrCode.getId()));
		QrCodeDto res = qrCodeMapper.mapEntToDto(fetchedQrCode);		
		return res;
	}

	// TODO: write some tests
	@Override
	@Transactional
	public String deleteById(String id) throws BadRequestApiException, DbApiException, NotFoundApiException {
		
		Optional<QrCode> qrCode = Optional.empty();
		try {
			long entryId = qrCodeMapper.decodeIds(id);
			qrCode = qrCodeRepo.findById(Long.valueOf(entryId));
			
			if (!qrCode.isPresent()) {
				logger.warn(String.format("QrCode with [id=%d] has not been found in the DB.", Long.valueOf(entryId)));
				throw new NotFoundApiException(notFoundInDbError);
			}
			
			QrCode qrCodeToDelete = qrCode.get();
			
			logger.info(String.format("About to delete the qrCode with [filename=%s] and [id=%d].", qrCodeToDelete.getFileName(), qrCodeToDelete.getId()));
			qrCodeRepo.delete(qrCodeToDelete);
			logger.info(String.format("QrCode with [filename=%s], and [id=%d] has been deleted.", qrCodeToDelete.getFileName(), qrCodeToDelete.getId()));
		} catch (BadRequestApiException exc) {
			throw new BadRequestApiException(exc.getMessage());
		} catch (NotFoundApiException exc) {
			throw new NotFoundApiException(exc.getMessage());
		} catch(Exception exc) {
			logger.error(exc.getStackTrace().toString());
			throw new DbApiException(dbError);
		}
		
		return id;
	}
	
	private void setCreatedByForNewQrCode(QrCode qrCode) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		qrCode.setCreatedBy(userDetails.getUsername());
	}
}
