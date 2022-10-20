package com.thales.qrapi.entities;

import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "qr_codes")
public class QrCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "content_type")
	private short contentType;

	@Lob // NOTE: performance improvements exist
	@Column(name = "byte_content")
	private byte[] byteContent;

	@Column(name = "text_content")
	private String textContent;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "vcard_id")
	private Vcard vCard;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
					CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "created_by", referencedColumnName = "username")
	private User user;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "deleted_date")
	private Timestamp deletedDate;

	public QrCode() {
	}

	public QrCode(String fileName, short contentType, byte[] byteContent, String textContent, User user,
			Timestamp createdDate) {
		super();
		this.fileName = fileName;
		this.contentType = contentType;
		this.byteContent = byteContent;
		this.textContent = textContent;
		this.user = user;
		this.createdDate = createdDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public short getContentType() {
		return contentType;
	}

	public void setContentType(short contentType) {
		this.contentType = contentType;
	}

	public byte[] getByteContent() {
		return byteContent;
	}

	public void setByteContent(byte[] byteContent) {
		this.byteContent = byteContent;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public Vcard getvCard() {
		return vCard;
	}

	public void setvCard(Vcard vCard) {
		this.vCard = vCard;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Timestamp deletedDate) {
		this.deletedDate = deletedDate;
	}

	@Override
	public String toString() {
		return "QrCode [id=" + id + ", fileName=" + fileName + ", contentType=" + contentType + ", byteContent="
				+ Arrays.toString(byteContent) + ", textContent=" + textContent + ", vCard=" + vCard + ", isDeleted="
				+ isDeleted + ", user=" + user + ", createdDate=" + createdDate + ", deletedDate=" + deletedDate + "]";
	}
}
