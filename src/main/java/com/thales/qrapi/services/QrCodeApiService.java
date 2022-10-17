package com.thales.qrapi.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thales.qrapi.dtos.enums.QrCodeContentType;
import com.thales.qrapi.dtos.qrcode.QrCodeContentHolder;
import com.thales.qrapi.dtos.qrcode.QrCodeDto;
import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.entities.Vcard;
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

	private static final String findInDbError = "An exception has occured while finding the entry in the database.";
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
			QrCodeContentHolder contentHolder = qrCodeHelper.extractQrCodeData(bytes);
			
			QrCode newQrCode = generateNewQrCode(bytes, file, contentHolder);

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
			throw new DbApiException(findInDbError);
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
		} catch (BadRequestApiException exc) {
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
			throw new DbApiException(findInDbError);
		}
		
		return id;
	}

	
	// Private Service Methods
	private QrCode generateNewQrCode(byte[] bytes, MultipartFile file, QrCodeContentHolder contentHolder) {
		String fileName = file.getOriginalFilename();
		
		// NOTE: code that will get me an authenticated user
//		Authentication authentication = 
//		        authenticationManager.authenticate(
//		            new UsernamePasswordAuthenticationToken(username, password)
//		        );
//
//		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		QrCode newQrCode = new QrCode(
			fileName,
			contentHolder.getContentType().getValue(),
			bytes,
			contentHolder.getContent(),
			"DavidTODO",	// TODO: username of the authenticated account must be inserted here
			new Timestamp(System.currentTimeMillis())
		);
		
		if (QrCodeContentType.getType(newQrCode.getContentType()) == QrCodeContentType.VCARD)
			setVcardQrCodeContent(newQrCode, contentHolder);
		
		return newQrCode;
	}
	
	private void setVcardQrCodeContent(QrCode qrCode, QrCodeContentHolder contentHolder) {
		Vcard vCard = new Vcard(contentHolder.getContent());
		
		qrCode.setTextContent(null);	// qrCode which carries vCard has a content in a separate table
		qrCode.setvCard(vCard); 		// the content is stored in vCard table
	}
}
