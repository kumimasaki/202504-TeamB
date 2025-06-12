package ec.com.model.entity;

import java.sql.Timestamp;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long adminId;
	private String adminName;
	private String adminEmail;
	private String adminPassword;
	private Integer deleteFlg;
	private Timestamp registerDate;
	
	public Long getAdminId() {
		return adminId;
	}


	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}


	public String getAdminName() {
		return adminName;
	}


	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}


	public String getAdminEmail() {
		return adminEmail;
	}


	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}


	public String getAdminPassword() {
		return adminPassword;
	}


	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}


	public Integer getDeleteFlg() {
		return deleteFlg;
	}


	public void setDeleteFlg(Integer deleteFlg) {
		this.deleteFlg = deleteFlg;
	}


	public Timestamp getRegisterDate() {
		return registerDate;
	}


	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}


	public Admin() {
	}


	public Admin(String adminName, String adminEmail, String adminPassword, Integer deleteFlg,
			Timestamp registerDate) {
		this.adminName = adminName;
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
		this.deleteFlg = deleteFlg;
		this.registerDate = registerDate;
	}
}
