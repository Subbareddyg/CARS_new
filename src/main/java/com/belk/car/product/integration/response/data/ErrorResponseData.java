package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the error in response information. This has a different xml response from the original
 * intended one. 
 * Current Path in xml response: /error
 */
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ErrorResponseData {
	
	private String errorCode;
	
	private String errorMessage;

	@XmlElement(name = "errorCode")
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@XmlElement(name = "errorString")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
