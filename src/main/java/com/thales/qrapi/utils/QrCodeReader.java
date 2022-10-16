package com.thales.qrapi.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.ServerApiException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@Component
public class QrCodeReader {
	
	private static final String errorReadingQr = "An error has occured while processing the uploaded file. Make sure that the file is a valid Qr code.";
	private static final String errorReadingFile = "An error has occured while reading the uploaded file.";
	
	public String readQRCode(byte[] bytes) throws ServerApiException, BadRequestApiException {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
					new BufferedImageLuminanceSource(bufferedImage)));
	        MultiFormatReader multiFormatReader = new MultiFormatReader();
	        
	        Result res = multiFormatReader.decode(binaryBitmap);
	        return res.getText();
		} catch(NotFoundException exc) {
			exc.printStackTrace();
			throw new BadRequestApiException(errorReadingQr);
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new ServerApiException(errorReadingFile);
		}
	}
}
