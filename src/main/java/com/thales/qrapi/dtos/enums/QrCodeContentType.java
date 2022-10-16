package com.thales.qrapi.dtos.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum QrCodeContentType {
	TEXT(0), URL(1), VCARD(2);
	
	private int value;
	private static final Map<Integer, QrCodeContentType> lookupMap
		= new HashMap<Integer, QrCodeContentType>();
	
	static {
		for(QrCodeContentType type: EnumSet.allOf(QrCodeContentType.class))
			lookupMap.put(type.getValue(), type);
	}
	
	private QrCodeContentType(final int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public static QrCodeContentType getType(int val) {
		return lookupMap.get(val);
	}
}
