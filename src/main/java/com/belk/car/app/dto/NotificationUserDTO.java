package com.belk.car.app.dto;

import java.math.BigDecimal;
import java.util.Date;



public class NotificationUserDTO implements java.io.Serializable {
	
	private String emailAddress;
	private String firstName;
	private String lastName;
	private BigDecimal createdCount;
	private BigDecimal updatedCount;
	private BigDecimal carCount;
	private BigDecimal carNumber;
	private Date dueDate;
	
	/**
	 * @return the carNumber
	 */
	public BigDecimal getCarNumber() {
		return carNumber;
	}
	/**
	 * @param carNumber the carNumber to set
	 */
	public void setCarNumber(BigDecimal carNumber) {
		this.carNumber = carNumber;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the createdCount
	 */
	public BigDecimal getCreatedCount() {
		return createdCount;
	}
	/**
	 * @param createdCount the createdCount to set
	 */
	public void setCreatedCount(BigDecimal createdCount) {
		this.createdCount = createdCount;
	}
	/**
	 * @return the updatedCount
	 */
	public BigDecimal getUpdatedCount() {
		return updatedCount;
	}
	/**
	 * @param updatedCount the updatedCount to set
	 */
	public void setUpdatedCount(BigDecimal updatedCount) {
		this.updatedCount = updatedCount;
	}
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @return the carCount
	 */
	public BigDecimal getCarCount() {
		return carCount;
	}
	/**
	 * @param carCount the carCount to set
	 */
	public void setCarCount(BigDecimal carCount) {
		this.carCount = carCount;
	}
	
	/**
	 * @return the DueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}
	/**
	 * @param DueDate the DueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	
}
