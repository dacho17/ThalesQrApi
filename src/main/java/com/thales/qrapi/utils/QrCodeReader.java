package com.thales.qrapi.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.exceptions.ServerApiException;

@Component
public class QrCodeReader {
	
	private static final String errorReadingQr = "An error has occured while processing the uploaded file. Make sure that the file is a valid Qr code.";
	private static final String errorReadingFile = "An error has occured while reading the uploaded file.";
	
	private static final Logger logger = LoggerFactory.getLogger(QrCodeReader.class);
	
	public String readQRCode(byte[] bytes) throws ServerApiException, BadRequestApiException {
		logger.info("Starting to read QrCode in byte format.");
		
		try {
			
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
					new BufferedImageLuminanceSource(bufferedImage)));
	        MultiFormatReader multiFormatReader = new MultiFormatReader();
	        
	        Result res = multiFormatReader.decode(binaryBitmap);

	        logger.info("QrCode has successfully been read.");
	        return res.getText();
		} catch(IOException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new BadRequestApiException(errorReadingQr);
		} catch(IllegalArgumentException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new BadRequestApiException(errorReadingQr);
		} catch(NotFoundException exc) {
			logger.error(exc.getStackTrace().toString());
			throw new BadRequestApiException(errorReadingQr);
		} catch(NullPointerException exc) {
			// occurs when bufferedImage was unsuccessfully created due to file not being an image
			logger.error(exc.getStackTrace().toString());
			throw new BadRequestApiException(errorReadingQr);
		} catch (Exception exc) {
			logger.error(exc.getStackTrace().toString());
			throw new ServerApiException(errorReadingFile);
		}
	}
}
