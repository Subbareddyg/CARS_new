package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 * POJO to hold the response information. It comprises the group header, entry level information
 * for the groupings request.
 * Current Path in xml response: /getGroupResponse/.
 */
@XmlRootElement(name = "getGroupResponse", namespace = "http://services.belkinc.com/product")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupIntegrationResponseData {
	
	private List<GroupCatalogData> responseList;
	
	private ErrorResponseData errorResponseData;
	
	@XmlElement(name = "Group_Catalog")
	public List<GroupCatalogData> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<GroupCatalogData> responseList) {
		this.responseList = responseList;
	}

	@XmlTransient
	public ErrorResponseData getErrorResponseData() {
		return errorResponseData;
	}

	public void setErrorResponseData(ErrorResponseData errorResponseData) {
		this.errorResponseData = errorResponseData;
	}
	
}
