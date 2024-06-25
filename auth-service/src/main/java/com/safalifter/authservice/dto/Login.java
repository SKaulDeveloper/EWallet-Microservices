package com.safalifter.authservice.dto; 

public class Login { 
	
	private String userName; 
	private String password; 
	
	private String grantType;
	
	public String getUserName() { 
		return userName; 
	} 
	public void setUserName(String userName) { 
		this.userName = userName; 
	} 
	public String getPassword() { 
		return password; 
	} 
	public void setPassword(String password) { 
		this.password = password; 
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	} 
	
} 
