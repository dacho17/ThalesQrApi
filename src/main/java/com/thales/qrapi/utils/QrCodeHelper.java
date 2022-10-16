package com.thales.qrapi.utils;

import java.net.MalformedURLException;
import java.net.URL; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thales.qrapi.dtos.QrCodeContentHolder;
import com.thales.qrapi.dtos.enums.QrCodeContentType;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.ServerApiException;

@Component
public class QrCodeHelper {
	
	@Autowired
	private QrCodeReader qrCodeReader;
	
	@Autowired
	private VcardParser vCardParser;
	
	public QrCodeContentHolder extractQrCodeData(byte[] bytes) throws BadRequestApiException, ServerApiException {
		
		// Setting contentType as text for now, and content is null
		QrCodeContentHolder contentHolder = new QrCodeContentHolder(QrCodeContentType.TEXT, null);
		
		try {
			String strContent = qrCodeReader.readQRCode(bytes);	// decode the file, throws ServerApiException, BadRequestApiException

			String xmlContent = vCardParser.parseStringContent(strContent);
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
			// catch MalformedURLException if creating URL fails - we know the content is not URL -> content remains text
			contentHolder.setContent(strContent);
		}
	}
}
