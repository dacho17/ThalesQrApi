package com.thales.qrapi.dtos;

import com.thales.qrapi.dtos.enums.QrCodeContentType;

public class QrCodeContentHolder {
	private QrCodeContentType contentType;
	private String content;
	
	public QrCodeContentHolder() {}
	
	public QrCodeContentHolder(QrCodeContentType contentType, String content) {
		super();
		this.contentType = contentType;
		this.content = content;
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

	@Override
	public String toString() {
		return "QrCodeContentHolder [contentType=" + contentType + ", content=" + content + "]";
	}
}
