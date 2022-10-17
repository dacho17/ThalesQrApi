package com.thales.qrapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "vcards")
public class Vcard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Lob // NOTE: performance improvements exist
	@Column(name = "xml_content")
	private String xmlContent;

	public Vcard() {
	}

	public Vcard(String xmlContent) {
		super();
		this.xmlContent = xmlContent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	@Override
	public String toString() {
		return "Vcard [id=" + id + ", xmlContent=" + xmlContent + "]";
	}
}
