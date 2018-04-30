/**
 * 
 */

package com.belk.car.app.webapp.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;
import org.appfuse.model.User;

import com.belk.car.app.dto.RequestDTO;
import com.belk.car.app.dto.StyleSkuDTO;

/**
 * @author afusy01
 */
@SuppressWarnings("unchecked")
public class StylesSkuForm implements Serializable {

	
	private static final long serialVersionUID = -7468071594442815764L;

	private String vendorNo;
	private String vendorName;
	private String serviceId;
	private String serviceName;
	private String noOfActiveStyles;
	private String noOfActiveSkus;
	private String styleId;
	private String styleDesc;
	private String idbStatus;
	private String vendorUpc;
	private String belkUpc;
	private String dropShipStatus;
	private Boolean userDeptsOnly;
	private String selectedStyle;
	private User user;
	private String role;

	/** Sku details */
	private String dateLastIDBUpdate;
	private String dateLastRequest;
	private String allSku;
	private String allColor;
	private String allSize;

	private boolean error;
	private List<StyleSkuDTO> list = LazyList.decorate(new ArrayList<StyleSkuDTO>(), FactoryUtils
			.instantiateFactory(StyleSkuDTO.class));

	private List<RequestDTO> requestDetails = LazyList.decorate(new ArrayList<RequestDTO>(),
			FactoryUtils.instantiateFactory(RequestDTO.class));

	/**
	 * @return the vendorNo
	 */
	public String getVendorNo() {
		return vendorNo;
	}

	/**
	 * @param vendorNo the vendorNo to set
	 */
	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the noOfActiveStyles
	 */
	public String getNoOfActiveStyles() {
		return noOfActiveStyles;
	}

	/**
	 * @param noOfActiveStyles the noOfActiveStyles to set
	 */
	public void setNoOfActiveStyles(String noOfActiveStyles) {
		this.noOfActiveStyles = noOfActiveStyles;
	}

	/**
	 * @return the noOfActiveSkus
	 */
	public String getNoOfActiveSkus() {
		return noOfActiveSkus;
	}

	/**
	 * @param noOfActiveSkus the noOfActiveSkus to set
	 */
	public void setNoOfActiveSkus(String noOfActiveSkus) {
		this.noOfActiveSkus = noOfActiveSkus;
	}

	/**
	 * @return the styleId
	 */
	public String getStyleId() {
		return styleId;
	}

	/**
	 * @param styleId the styleId to set
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	/**
	 * @return the styleDesc
	 */
	public String getStyleDesc() {
		return styleDesc;
	}

	/**
	 * @param styleDesc the styleDesc to set
	 */
	public void setStyleDesc(String styleDesc) {
		this.styleDesc = styleDesc;
	}

	/**
	 * @return the idbStatus
	 */
	public String getIdbStatus() {
		return idbStatus;
	}

	/**
	 * @param idbStatus the idbStatus to set
	 */
	public void setIdbStatus(String idbStatus) {
		this.idbStatus = idbStatus;
	}

	/**
	 * @return the vendorUpc
	 */
	public String getVendorUpc() {
		return vendorUpc;
	}

	/**
	 * @param vendorUpc the vendorUpc to set
	 */
	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}

	/**
	 * @return the belkUpc
	 */
	public String getBelkUpc() {
		return belkUpc;
	}

	/**
	 * @param belkUpc the belkUpc to set
	 */
	public void setBelkUpc(String belkUpc) {
		this.belkUpc = belkUpc;
	}

	/**
	 * @return the dropShipStatus
	 */
	public String getDropShipStatus() {
		return dropShipStatus;
	}

	/**
	 * @param dropShipStatus the dropShipStatus to set
	 */
	public void setDropShipStatus(String dropShipStatus) {
		this.dropShipStatus = dropShipStatus;
	}

	/**
	 * @return the userDeptsOnly
	 */
	public Boolean getUserDeptsOnly() {
		return userDeptsOnly;
	}

	/**
	 * @param userDeptsOnly the userDeptsOnly to set
	 */
	public void setUserDeptsOnly(Boolean userDeptsOnly) {
		this.userDeptsOnly = userDeptsOnly;
	}

	/**
	 * @param selectedStyle the selectedStyle to set
	 */
	public void setSelectedStyle(String selectedStyle) {
		this.selectedStyle = selectedStyle;
	}

	/**
	 * @return the selectedStyle
	 */
	public String getSelectedStyle() {
		return selectedStyle;
	}

	/**
	 * @return the list
	 */
	public List<StyleSkuDTO> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<StyleSkuDTO> list) {
		this.list = list;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @return the dateLastIDBUpdate
	 */
	public String getDateLastIDBUpdate() {
		return dateLastIDBUpdate;
	}

	/**
	 * @param dateLastIDBUpdate the dateLastIDBUpdate to set
	 */
	public void setDateLastIDBUpdate(String dateLastIDBUpdate) {
		this.dateLastIDBUpdate = dateLastIDBUpdate;
	}

	/**
	 * @return the dateLastRequest
	 */
	public String getDateLastRequest() {
		return dateLastRequest;
	}

	/**
	 * @param dateLastRequest the dateLastRequest to set
	 */
	public void setDateLastRequest(String dateLastRequest) {
		this.dateLastRequest = dateLastRequest;
	}

	/**
	 * @return the allSku
	 */
	public String getAllSku() {
		return allSku;
	}

	/**
	 * @param allSku the allSku to set
	 */
	public void setAllSku(String allSku) {
		this.allSku = allSku;
	}

	/**
	 * @return the allColor
	 */
	public String getAllColor() {
		return allColor;
	}

	/**
	 * @param allColor the allColor to set
	 */
	public void setAllColor(String allColor) {
		this.allColor = allColor;
	}

	/**
	 * @return the allSize
	 */
	public String getAllSize() {
		return allSize;
	}

	/**
	 * @param allSize the allSize to set
	 */
	public void setAllSize(String allSize) {
		this.allSize = allSize;
	}

	/**
	 * @return the requestDetails
	 */
	public List<RequestDTO> getRequestDetails() {
		return requestDetails;
	}

	/**
	 * @param requestDetails the requestDetails to set
	 */
	public void setRequestDetails(List<RequestDTO> requestDetails) {
		this.requestDetails = requestDetails;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
}
