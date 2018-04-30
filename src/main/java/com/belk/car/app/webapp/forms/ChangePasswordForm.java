package com.belk.car.app.webapp.forms;

import java.io.Serializable;

import org.appfuse.model.User;

public class ChangePasswordForm implements Serializable {

	private String emailAddress ;
	private String password;
	private String confirmPassword;
	
	private User user;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
