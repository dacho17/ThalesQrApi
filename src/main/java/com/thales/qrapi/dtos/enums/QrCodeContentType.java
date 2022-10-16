package com.thales.qrapi.dtos.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum QrCodeContentType {
	TEXT((short)0), URL((short)1), VCARD((short)2);
	
	private short value;
	
	private static final Map<Short, QrCodeContentType> lookupMap
		= new HashMap<Short, QrCodeContentType>();
	
	static {
		for(QrCodeContentType type: EnumSet.allOf(QrCodeContentType.class))
			lookupMap.put(type.getValue(), type);
	}
	
	private QrCodeContentType(final short value) {
		this.value = value;
	}
	
	public short getValue() {
		return value;
	}

	public static QrCodeContentType getType(short val) {
		return lookupMap.get(val);
	}
}
