package com.thales.qrapi.dtos.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum UserRole {
	ROLE_STUDENT((short)0),	ROLE_TEACHER((short)1), ROLE_ADMIN((short)2);

	private short value;

	private static final Map<Short, UserRole> lookupMap
		= new HashMap<Short, UserRole>();

	static {
		for(UserRole type: EnumSet.allOf(UserRole.class))
			lookupMap.put(type.getValue(), type);
	}
	
	private UserRole(final short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static UserRole getRole(short val) {
		return lookupMap.get(val);
	}
}
