/**
 * 
 */

package com.belk.car.app.webapp.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.belk.car.app.dto.RequestDTO;

/**
 * @author afusy01
 */
@SuppressWarnings("unchecked")
public class RequestHistoryForm implements Serializable {

	
	private static final long serialVersionUID = 5527132119539384710L;
	private String vendorNo;
	private String vendorName;
	private String serviceId;
	private String serviceName;
	private String dateCreated;
	private String createdBy;
	private String updatedDate;
	private String updatedBy;
	private String requestId;
	private String requestDesc;
	private String effectiveStartDate;
	private String effectiveEndDate;
	private String styleId;
	private String styleDesc;
	private String vendorUpc;
	private String belkUpc;
	private String status;
	// Added by Priyanka Gadia
	private String statusFilter;
	private boolean error;
	private String role;
	private List<RequestDTO> requestDetails = LazyList.decorate(new ArrayList(), FactoryUtils
			.instantiateFactory(RequestDTO.class));

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
	 * @return the dateCreated
	 */
	public String getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the updatedDate
	 */
	public String getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the requestDesc
	 */
	public String getRequestDesc() {
		return requestDesc;
	}

	/**
	 * @param requestDesc the requestDesc to set
	 */
	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}

	/**
	 * @return the effectiveStartDate
	 */
	public String getEffectiveStartDate() {
		return effectiveStartDate;
	}

	/**
	 * @param effectiveStartDate the effectiveStartDate to set
	 */
	public void setEffectiveStartDate(String effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	/**
	 * @return the effectiveEndDate
	 */
	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}

	/**
	 * @param effectiveEndDate the effectiveEndDate to set
	 */
	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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

	public void setStatusFilter(String statusFilter) {
		this.statusFilter = statusFilter;
	}

	public String getStatusFilter() {
		return statusFilter;
	}

}
