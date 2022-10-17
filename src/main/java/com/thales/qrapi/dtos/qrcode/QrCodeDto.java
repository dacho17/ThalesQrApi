package com.thales.qrapi.dtos.qrcode;

import com.thales.qrapi.dtos.enums.QrCodeContentType;

public class QrCodeDto {
	private String id;
	private String fileName;
	private QrCodeContentType contentType;
	private String content;	// NOTE: Datatype of this property was initially considered to be generic so to be able to carry String/JSON/XML/Vcard custom defined object
	// private byte[] byteContent;
	private String createdBy;
	private String createdDate;
	
	public QrCodeDto() {}

	public QrCodeDto(String id, String fileName, QrCodeContentType contentType, String content, String createdBy,
			String createdDate) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.contentType = contentType;
		this.content = content;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public QrCodeContentType getContentType() {
		return contentType;
	}

	public void setContentType(QrCodeContentType contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "QrCodeDto [id=" + id + ", fileName=" + fileName + ", contentType=" + contentType + ", content="
				+ content + ", createdBy=" + createdBy + ", createdDate=" + createdDate + "]";
	}
}
