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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "/qr-codes", produces = { "application/json" })
public class QrCodeController {
	// "This Controller Exposes QrCode CRUD Functionalities"
	
	private static final String createQrCodeSummary = "Upload the qr code. If the operation is successful, the endpoint returns the integer Id of the uploaded qr code. Available to authenticated users.";
	private static final String getAllSummary = "Get list of uploaded qr codes. If the operation is successful, the endpoint returns the list of uploaded qr codes without their respective content.";
	private static final String getOneSummary = "Get the specific uploaded qr code. If the operation is successful, the endpoint returns qrCode object that contains both encoded and decoded qr code.";
	private static final String deleteQrCodeSummary = "Delete the qr code. If the operation is successful, the endpoint returns the integer Id of the deleted qr code. Available only to Admin users.";

	private static final String uploadSuccess = "Qr code uploaded successfully.";
	private static final String getAllSuccess = "List of uploaded qr codes successfully retrieved.";
	private static final String getOneSuccess = "Requested qr code successfully retrieved.";
	private static final String deleteSuccess = "Qr code deleted successfully.";
	
	private static final String errorInRequest = "Error detected in the request.";
	private static final String noFileInRequest = "No file has been provided to be uploaded.";
	private static final String unauthenticatedUser = "This user is unauthenticated and can not perform the requested operation.";
	private static final String unauthorizedUser = "This user is unauthorized to perform the requested operation.";
	private static final String qrCodeNotFound = "Requested qr code not found.";
	private static final String internalServerError = "Internal Server Error occurred.";
	
	private static final String httpOK = "200";
	private static final String httpCreated = "201";
	private static final String httpBadReq = "400";
	private static final String httpNotAuthenicated = "401";
	private static final String httpNotAuthorized = "403";
	private static final String httpNotFound = "404";
	private static final String httpServerError = "500";
	
	@Autowired
	private QrCodeService<String, QrCodeDto> qrCodeService;
	
	@Operation(summary = createQrCodeSummary)
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
		@ApiResponse(responseCode = httpCreated, description = uploadSuccess, content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)),}),
		@ApiResponse(responseCode = httpBadReq, description = errorInRequest, content = @Content),
		@ApiResponse(responseCode = httpNotAuthenicated, description = unauthenticatedUser, content = @Content),
		@ApiResponse(responseCode = httpServerError, description = internalServerError, content = @Content) })
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping(consumes = "multipart/form-data")
	public ResponseObject<String> createQrCode(@RequestParam("file") MultipartFile file) throws Exception {

		if (file == null || file.getSize() == 0) {
			throw new BadRequestApiException(noFileInRequest);
		}
		
		String id = qrCodeService.save(file);
		
		return new ResponseObject<>(uploadSuccess, id);
	}
	
	@Operation(summary = getAllSummary)
	@ApiResponses(value = {
		@ApiResponse(responseCode = httpOK, description = getAllSuccess, content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))}),
		@ApiResponse(responseCode = httpBadReq, description = errorInRequest, content = @Content),
		@ApiResponse(responseCode = httpServerError, description = internalServerError, content = @Content) })
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping
	public ResponseObject<List<QrCodeDto>> getQrCodes() {
		
		List<QrCodeDto> res = qrCodeService.findAll();
		
		return new ResponseObject<>(getAllSuccess, res);
	}
	
	@Operation(summary = getOneSummary)
	@ApiResponses(value = {
			@ApiResponse(responseCode = httpOK, description = getOneSuccess, content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))}),
			@ApiResponse(responseCode = httpBadReq, description = errorInRequest, content = @Content),
			@ApiResponse(responseCode = httpNotFound, description = qrCodeNotFound, content = @Content),
			@ApiResponse(responseCode = httpServerError, description = internalServerError, content = @Content) })
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/{id}")
	public ResponseObject<QrCodeDto> getQrCode(@PathVariable String id) throws Exception {
		
			QrCodeDto res = qrCodeService.findById(id);
			
			return new 	ResponseObject<>(getOneSuccess, res);
	}
	
	@Operation(summary = deleteQrCodeSummary)
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
		@ApiResponse(responseCode = httpOK, description = deleteSuccess, content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class))}),
		@ApiResponse(responseCode = httpBadReq, description = errorInRequest, content = @Content),
		@ApiResponse(responseCode = httpNotAuthenicated, description = unauthenticatedUser, content = @Content),
		@ApiResponse(responseCode = httpNotAuthorized, description = unauthorizedUser, content = @Content),
		@ApiResponse(responseCode = httpNotFound, description = qrCodeNotFound, content = @Content),
		@ApiResponse(responseCode = httpServerError, description = internalServerError, content = @Content) })
	@ResponseStatus(code = HttpStatus.OK)
	@DeleteMapping("/{id}")
	public ResponseObject<String> deleteQrCode(@PathVariable String id) throws Exception {
		
		String res = qrCodeService.deleteById(id);
		
		return new 	ResponseObject<>(deleteSuccess, res);
	}
}
