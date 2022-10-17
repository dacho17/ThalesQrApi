package com.thales.qrapi.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.thales.qrapi.dtos.enums.QrCodeContentType;
import com.thales.qrapi.dtos.qrcode.QrCodeContentHolder;
import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.entities.Vcard;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.ServerApiException;

@Component
public class QrCodeHelper {
	
	@Autowired
	private QrCodeReader qrCodeReader;
	
	@Autowired
	private VcardParser vCardParser;
	
	public QrCode generateNewQrCode(byte[] bytes, MultipartFile file) throws BadRequestApiException, ServerApiException {
		
		QrCodeContentHolder contentHolder = extractQrCodeData(bytes);
		String fileName = file.getOriginalFilename();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		QrCode newQrCode = new QrCode(
			fileName,
			contentHolder.getContentType().getValue(),
			bytes,
			contentHolder.getContent(),
			userDetails.getUsername(),
			new Timestamp(System.currentTimeMillis())
		);
		
		if (QrCodeContentType.getType(newQrCode.getContentType()) == QrCodeContentType.VCARD)
			setVcardQrCodeContent(newQrCode, contentHolder);	// NOTE: if contentType is vcard, transfer the content to vcard object
		
		return newQrCode;	
	}
	
	private QrCodeContentHolder extractQrCodeData(byte[] bytes) throws BadRequestApiException, ServerApiException {
		
		// NOTE: Setting contentType as text for now, and content is null
		QrCodeContentHolder contentHolder = new QrCodeContentHolder(QrCodeContentType.TEXT, null);
		
		try {
			String strContent = qrCodeReader.readQRCode(bytes);

			String xmlContent = vCardParser.parseStringContent(strContent);	// NOTE: a way to check if content is a vcard
			if (xmlContent != null) {
				contentHolder.setContentType(QrCodeContentType.VCARD);
				contentHolder.setContent(xmlContent);				
			} else {
				setUrlOrTextContent(strContent, contentHolder);
			}
		} catch(BadRequestApiException exc) {
			exc.printStackTrace();
			throw new BadRequestApiException(exc.getMessage());
		} catch(ServerApiException exc) {
			exc.printStackTrace();
			throw new ServerApiException(exc.getMessage());
		}
		
		return contentHolder;
	}
	
	private void setUrlOrTextContent(String strContent, QrCodeContentHolder contentHolder) {
		try {
			URL urlContent = new URL(strContent);
			
			contentHolder.setContentType(QrCodeContentType.URL);
			contentHolder.setContent(urlContent.toString());
		} catch (MalformedURLException exc) {
			// NOTE: catch MalformedURLException if creating URL fails - we know the content is not URL -> content remains text
			contentHolder.setContent(strContent);
		}
	}

	private void setVcardQrCodeContent(QrCode qrCode, QrCodeContentHolder contentHolder) {
		Vcard vCard = new Vcard(contentHolder.getContent());
		
		qrCode.setTextContent(null);	// NOTE: qrCode which carries vCard has a content in a separate table
		qrCode.setvCard(vCard); 		// NOTE: the content is stored in vCard table
	}
}
