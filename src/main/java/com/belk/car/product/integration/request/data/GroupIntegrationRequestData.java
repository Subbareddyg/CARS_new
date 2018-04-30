package com.belk.car.product.integration.request.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * POJO to be able to create the required xml structure for the grouping integration with the PIM system. 
 * This will handle all current requests with the new grouping service.
 */
@XmlRootElement(name = "getGroupRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupIntegrationRequestData {
	
	private String requestType;
	
	private List<String> inputData;
	
	private List<PackGroupRequestData> inputPackData;
	
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
	public List<PackGroupRequestData> getInputPackData() {
		return inputPackData;
	}
	public void setInputPackData(List<PackGroupRequestData> inputPackData) {
		this.inputPackData = inputPackData;
	}
}
