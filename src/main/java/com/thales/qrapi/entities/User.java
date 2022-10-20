package com.thales.qrapi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "role_type")
	private short roleType;
	
	@OneToMany(mappedBy = "user",
			cascade = {CascadeType.PERSIST, CascadeType.MERGE,
					CascadeType.DETACH, CascadeType.REFRESH})
	private List<QrCode> qrCodes;
	
	public User() {
	}

	public User(String username, String password, short roleType) {
		super();
		this.username = username;
		this.password = password;
		this.roleType = roleType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public short getRoleType() {
		return roleType;
	}

	public void setRoleType(short roleType) {
		this.roleType = roleType;
	}

	public List<QrCode> getQrCodes() {
		return qrCodes;
	}

	public void setQrCodes(List<QrCode> qrCodes) {
		this.qrCodes = qrCodes;
	}
	
	public void add(QrCode qrCode) {
		if (qrCodes == null) {
			qrCodes = new ArrayList<>();
		}
		
		qrCodes.add(qrCode);
		qrCode.setUser(this);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", roleType=" + roleType
				+ ", qrCodes=" + qrCodes + "]";
	}
}
