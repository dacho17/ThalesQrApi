package com.thales.qrapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ezvcard.Ezvcard;
import ezvcard.VCard;

@Component
public class VcardParser {

	private static final Logger logger = LoggerFactory.getLogger(VcardParser.class);
	
	// NOTE: if no vcard is found in the qrcode content, return null and handle the case in a non-vcard way in the calling func.
	public String parseStringContent(String strContent) {
		VCard vcard = Ezvcard.parse(strContent).first();
		
		if (vcard == null) {
			logger.warn("The content parsed is not VCARD.");
			return null;
		}
		
		String xmlInStringFormat = Ezvcard.writeXml(vcard).go();
		logger.info(String.format("Successfully parsed VCARD qrContent and extracted XML from it [xmlContent=%s].", xmlInStringFormat));
		
		return xmlInStringFormat;
	}
	
	
	// NOTE: NOT USED in the solution! - method used only for testing. An example method.
	// Moving forward it could prove useful if fine grained data will need to be extracted from Vcards
	public String getName(String strContent) {
		VCard vcard = Ezvcard.parse(strContent).first();
		
		return vcard.getFormattedName().toString();
	}
}
