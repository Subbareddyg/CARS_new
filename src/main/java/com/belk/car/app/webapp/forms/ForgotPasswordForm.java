package com.belk.car.app.webapp.forms;

import java.io.Serializable;

import org.appfuse.model.User;

public class ForgotPasswordForm implements Serializable {

	private String emailAddress ;
	
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
	
}
