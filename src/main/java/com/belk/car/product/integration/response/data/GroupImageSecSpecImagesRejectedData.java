package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group image spec component section.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Image_Sec_Spec/Images/Rejected.
 */
@XmlRootElement(name = "Rejected")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupImageSecSpecImagesRejectedData {
	
	private String rejectType;
	
	private String reason;
	
	private String date;
	
	@XmlElement(name = "Reject_Type")
	public String getRejectType() {
		return rejectType;
	}

	public void setRejectType(String rejectType) {
		this.rejectType = rejectType;
	}
	
	@XmlElement(name = "Reason")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@XmlElement(name = "Date")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
