package com.thales.qrapi.utils.interfaces;

import com.thales.qrapi.entities.QrCode;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.ServerApiException;

public interface QrCodeHelper {
	QrCode generateNewQrCode(byte[] bytes, String fileName) throws BadRequestApiException, ServerApiException;
}
