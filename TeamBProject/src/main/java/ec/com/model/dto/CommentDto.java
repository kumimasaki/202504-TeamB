package ec.com.model.dto;

import java.sql.Timestamp;

public class CommentDto {
	private String userName;
	private String context;
	private Timestamp registerDate;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public Timestamp getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}
	
	public CommentDto() {
	}
	
	public CommentDto(String userName, String context, Timestamp registerDate) {
		this.userName = userName;
		this.context = context;
		this.registerDate = registerDate;
	}
}
