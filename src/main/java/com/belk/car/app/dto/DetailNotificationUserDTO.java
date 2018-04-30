package com.belk.car.app.dto;

import java.util.List;
import com.belk.car.app.model.Car;

/**
 * 
 * @author AFUSY12
 *
 */

public class DetailNotificationUserDTO implements java.io.Serializable {
	
	private static final long serialVersionUID = 509429990081639843L;
	
	private Long userId;
	private String userType; 
	private	String firstName;
	private	String lastName; 
	private	String emailAddress;
	private List<Car> cars;
	
	public List<Car> getCars() {
		return cars;
	}
	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	
	
	
	

}
