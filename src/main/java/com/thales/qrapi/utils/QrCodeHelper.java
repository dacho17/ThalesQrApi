package com.thales.qrapi.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thales.qrapi.dtos.enums.QrCodeContentType;
import com.thales.qrapi.dtos.qrcode.QrCodeContentHolder;
import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.entities.Vcard;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.ServerApiException;

@Component
public class QrCodeHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(QrCodeHelper.class);
	
	@Autowired
	private QrCodeReader qrCodeReader;
	
	@Autowired
	private VcardParser vCardParser;
	
	public QrCode generateNewQrCode(byte[] bytes, String fileName) throws BadRequestApiException, ServerApiException {
		logger.info(String.format("Starting to generate a new QrCode object from the [file=%s].", fileName));
		
		QrCodeContentHolder contentHolder = extractQrCodeData(bytes);

		QrCode newQrCode = new QrCode(
			fileName,
			contentHolder.getContentType().getValue(),
			bytes,
			contentHolder.getContent(),
			null,	// for the moment, username will be null. Service layer will set it to a value
			new Timestamp(System.currentTimeMillis())
		);
		
		if (QrCodeContentType.getType(newQrCode.getContentType()) == QrCodeContentType.VCARD)
			setVcardQrCodeContent(newQrCode, contentHolder);	// NOTE: if contentType is vcard, transfer the content to vcard object
		
		logger.info(String.format("New QrCode of type [type=%s] object successfully generated for the file [file=%s].",
				QrCodeContentType.getType(newQrCode.getContentType()), fileName));
		return newQrCode;	
	}
	
	private QrCodeContentHolder extractQrCodeData(byte[] bytes) throws BadRequestApiException, ServerApiException {
		
		// NOTE: Setting contentType as text for now, and content is null
		QrCodeContentHolder contentHolder = new QrCodeContentHolder(QrCodeContentType.TEXT, null);
		
		try {
			String strContent = qrCodeReader.readQRCode(bytes);

			String xmlContent = vCardParser.parseStringContent(strContent);	// NOTE: a way to check if content is a vcard
			if (xmlContent != null) {
				logger.info("vCardParser found vcard in the uploaded qrCode image. The content is of VCARD type.");
				
				contentHolder.setContentType(QrCodeContentType.VCARD);
				contentHolder.setContent(xmlContent);				
			} else {
				setUrlOrTextContent(strContent, contentHolder);
			}
		} catch(BadRequestApiException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new BadRequestApiException(exc.getMessage());
		} catch(ServerApiException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new ServerApiException(exc.getMessage());
		}
		
		return contentHolder;
	}
	
	private void setUrlOrTextContent(String strContent, QrCodeContentHolder contentHolder) {
		try {
			URL urlContent = new URL(strContent);
			
			logger.info("Parsing of URL succedeed.The qrCode content is of type URL.");
			contentHolder.setContentType(QrCodeContentType.URL);
			contentHolder.setContent(urlContent.toString());
		} catch (MalformedURLException exc) {
			// NOTE: catch MalformedURLException if creating URL fails - we know the content is not URL -> content remains text
			logger.info("Parsing URL failed, we infer that the qrCode content is of type TEXT.");
			contentHolder.setContent(strContent);
		}
	}

	private void setVcardQrCodeContent(QrCode qrCode, QrCodeContentHolder contentHolder) {
		logger.info("QrCode content is found to be of type VCARD.");
		
		Vcard vCard = new Vcard(contentHolder.getContent());
		
		qrCode.setTextContent(null);	// NOTE: qrCode which carries vCard has a content in a separate table
		qrCode.setvCard(vCard); 		// NOTE: the content is stored in vCard table
	}
}
