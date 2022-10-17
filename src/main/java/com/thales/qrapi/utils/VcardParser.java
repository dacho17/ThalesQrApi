package com.thales.qrapi.utils;

import org.springframework.stereotype.Component;

import ezvcard.Ezvcard;
import ezvcard.VCard;

@Component
public class VcardParser {

	// NOTE: if no vcard is found in the qrcode content, return null and handle the case in a non-vcard way in the calling func.
	public String parseStringContent(String strContent) {
		VCard vcard = Ezvcard.parse(strContent).first();
		
		if (vcard == null) {
			return null;
		}
		
		String xmlInStringFormat = Ezvcard.writeXml(vcard).go();
		return xmlInStringFormat;
	}
}
