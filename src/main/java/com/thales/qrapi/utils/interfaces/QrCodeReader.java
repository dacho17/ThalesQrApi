package com.thales.qrapi.utils.interfaces;

import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.ServerApiException;

public interface QrCodeReader {
	public String readQRCode(byte[] bytes) throws ServerApiException, BadRequestApiException;
}
