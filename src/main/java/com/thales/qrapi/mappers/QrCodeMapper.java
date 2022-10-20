package com.thales.qrapi.mappers;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thales.qrapi.dtos.enums.QrCodeContentType;
import com.thales.qrapi.dtos.qrcode.QrCodeDto;
import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.entities.User;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.NotFoundApiException;

@Component
public class QrCodeMapper {
	
	private static final String hashIdError = "An exception has occured while encoding/decoding the provided id.";
	private static final String faultyId = "Requested qr code not found.";
	private static final String unknownUploader = "unknown";
	
	private static final Logger logger = LoggerFactory.getLogger(QrCodeMapper.class);
	
	@Value("${mapper.id.hashSalt}")
	private String hashSalt;
	
	public List<QrCodeDto> mapEntListToDto(List<QrCode> lst) {
		return lst.stream().map(qrCode -> mapEntToDto(qrCode)).collect(Collectors.toList());
	}

	public QrCodeDto mapEntToDto(QrCode qrCode) {
		String hashedId = encodeId(qrCode.getId());

		User uploader = qrCode.getUser();
		
		QrCodeContentType contentType = QrCodeContentType.getType(qrCode.getContentType());
		QrCodeDto res = new QrCodeDto(
			hashedId,
			qrCode.getFileName(),
			contentType,
			null,
			uploader != null ? uploader.getUsername() : unknownUploader,
			new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(qrCode.getCreatedDate())
		);
		
		// NOTE: Set the content based on the content type of this QrCode
		if (res.getContentType() == QrCodeContentType.VCARD) {
			res.setContent(qrCode.getvCard().getXmlContent());
		} else {
			res.setContent(qrCode.getTextContent());
		}
		
		return res;
	}

	public long decodeIds(String id) throws BadRequestApiException, NotFoundApiException {
		
		if (id == null || id.length() < 1) {
			logger.error(String.format("An invalid identifier has been received from the client [id=%id].", id));
			throw new BadRequestApiException(hashIdError);
		}
		
		Hashids hash = new Hashids(hashSalt);
		long[] ids = hash.decode(id);
		
		if (ids.length < 1) {
			// NOTE: if here -> id was provided -> the hash is unrecognized and the entry could not have been encoded with the hash
			logger.error(String.format("Id provided in the request [id=%s] could not be decoded. An entry with the provided Id does not exist.", id));
			throw new NotFoundApiException(faultyId);
		}
		
		return ids[0];
	}
	
	public String encodeId(long id) {
		Hashids hash = new Hashids(hashSalt);
		
		return hash.encode(id);
	}
}
