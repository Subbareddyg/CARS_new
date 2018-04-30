package com.belk.car.product.integration.request.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * POJO to be able to create the required xml structure for the integration with the PIM system. 
 * This will handle all current requests with the new service.
 */
@XmlRootElement(name = "getItemRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class IntegrationRequestData {
	
	private String requestType;
	
	private List<String> inputData;
	private List<PackItemRequestData> inputPackData;

	@XmlElement(name = "requestType")
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@XmlElement(name = "inputData")
	public List<String> getInputData() {
		return inputData;
	}

	public void setInputData(List<String> inputData) {
		this.inputData = inputData;
	}
	@XmlElement(name = "pack_data")
	public List<PackItemRequestData> getInputPackData() {
		return inputPackData;
	}
	public void setInputPackData(List<PackItemRequestData> inputPackData) {
		this.inputPackData = inputPackData;
	}
	
	

}
