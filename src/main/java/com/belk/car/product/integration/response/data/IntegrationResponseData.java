package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * POJO to hold the response information. It comprises the item header, entry level information
 * for the request. 
 * Current Path in xml response: /getItemRespone/.
 */
@XmlRootElement(name = "getItemResponse", namespace = "http://services.belkinc.com/product")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class IntegrationResponseData {
	
	private List<ItemCatalogData> responseList;
	
	private ErrorResponseData errorResponseData;
	private String imageChanged;
	
	@XmlElement(name = "Item_Catalog")
	public List<ItemCatalogData> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<ItemCatalogData> responseList) {
		this.responseList = responseList;
	}

	@XmlTransient
	public ErrorResponseData getErrorResponseData() {
		return errorResponseData;
	}

	public void setErrorResponseData(ErrorResponseData errorResponseData) {
		this.errorResponseData = errorResponseData;
	}

	public String getImageChanged() {
		return imageChanged;
	}

	public void setImageChanged(String imageChanged) {
		this.imageChanged = imageChanged;
	}
	
}
