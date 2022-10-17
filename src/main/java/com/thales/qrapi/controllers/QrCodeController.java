package com.thales.qrapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thales.qrapi.dtos.ResponseObject;
import com.thales.qrapi.dtos.qrcode.QrCodeDto;
import com.thales.qrapi.exceptions.BadRequestApiException;
import com.thales.qrapi.services.interfaces.QrCodeService;

@RestController
@RequestMapping(value = "/qr-codes", produces = { "application/json" })
public class QrCodeController {
	
	private static final String noFileInRequest = "No file has been provided to be uploaded.";
	
	@Autowired
	private QrCodeService<String, QrCodeDto> qrCodeService;
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping
	public ResponseObject<String> createQrCode(@RequestParam("file") MultipartFile file) throws Exception {

		if (file == null || file.getSize() == 0) {
			throw new BadRequestApiException(noFileInRequest);
		}
		
		String id = qrCodeService.save(file);
		
		return new ResponseObject<>("Qr code uploaded successfully.", id);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping
	public ResponseObject<List<QrCodeDto>> getQrCodes() {
		
		List<QrCodeDto> res = qrCodeService.findAll();
		
		return new ResponseObject<>("Qr codes successfully fetched.", res);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/{id}")
	public ResponseObject<QrCodeDto> getQrCode(@PathVariable String id) throws Exception {
		
			QrCodeDto res = qrCodeService.findById(id);
			
			return new 	ResponseObject<>("Qr code fetched successfully.", res);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@DeleteMapping("/{id}")
	public ResponseObject<String> deleteQrCode(@PathVariable String id) throws Exception {
		
		String res = qrCodeService.deleteById(id);
		
		return new 	ResponseObject<>("Qr code deleted successfully.", res);
	}
}
