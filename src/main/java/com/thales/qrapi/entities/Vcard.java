package com.thales.qrapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="vcards")
public class Vcard {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@OneToOne
	@JoinColumn(name="qr_code_id")
	private QrCode qrCode;
	
	@Lob	// NOTE: performance improvements exist
	@Column(name="xml_content")
	private String xmlContent;
	
	public Vcard() {}

	public Vcard(int id, QrCode qrCode, String xmlContent) {
		super();
		this.qrCode = qrCode;
		this.xmlContent = xmlContent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public QrCode getQrCode() {
		return qrCode;
	}

	public void setQrCode(QrCode qrCode) {
		this.qrCode = qrCode;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	@Override
	public String toString() {
		return "Vcard [id=" + id + ", qrCode=" + qrCode + ", xmlContent=" + xmlContent + "]";
	}
}
