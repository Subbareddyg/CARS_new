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

import com.belk.car.app.dto.StylesDTO;
import com.belk.car.app.model.oma.ItemRequestWorkflow;
import com.belk.car.app.model.oma.VFIRStyle;


/**
 * @author afusy01
 * This is a command class for 'ADD ITEM REQUEST' Screen.
 */
public class ItemRequestForm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1043027680073157591L;
	private String createDate ;
	private String description;
	private String sourceId;
	private String itemAction;
	private String merchandise;
	private String minimumMarkup;
	private String stylePopMethodId;
	private byte[] file;
	private String locationName;
	private Boolean userdepts;
	private String exceptionReason;
	private String requestId;
	private String createdBy;
	private String vendorNumber;
	private String serviceId;
	private String status;
	private String createdDate;
	private String vendorName;
	private String serviceName;
	private String updatedDate;
	private String updatedBy;
	private String filePath;
	private String requestStatus;
	private String postedBy;
	private String postedDate;
	private String showTable;
	private String uploadedStyles;
	private String failedStyles;
	private String validStyles;
	private String rejectReason;
	private Boolean enableDisableSkuException;
	private User user;
	private boolean error;
	private String role;
	private String approveRole;
	private int pageNo;
	private Integer totalResultSize;
	
	/**Item request style properties*/
	private List<VFIRStyle> styleDetails = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(VFIRStyle.class));
	
	/**Sku exception properties*/
	private List<StylesDTO> skuDetails = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(StylesDTO.class));
	
	/**Request update history*/
	private List<ItemRequestWorkflow> updateDetails = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(ItemRequestWorkflow.class));
	
	private List<String> selectedStyles;
	
	
	
	//Following method might need, if toString is to be override.
	//@Override
	/*public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n----------------------------------------------------------");
		buffer.append("\nEffective date:" + this.getCreateDate());
		buffer.append("\nUser departments:" + this.getUserdepts());
		buffer.append("\nAction:" + this.getItemAction());
		buffer.append("\nCreated By:" + this.getCreatedBy());
		buffer.append("\nCreated date:" + this.getCreatedDate());
		buffer.append("\nDescription:" + this.getDescription());
		buffer.append("\nException reasons:" + this.getExceptionReason());
		buffer.append("\nFile path:" + this.getFilePath());
		buffer.append("\nPrevious request id:" + this.getLocationName());
		buffer.append("\nOption:" + this.getMerchandise());
		buffer.append("\nPercentage:" + this.getMinimumMarkup());
		buffer.append("\nRequest Id:" + this.getRequestId());
		buffer.append("\nFulfillment service id:" + this.getServiceId());
		buffer.append("\nFulfillment service name:" + this.getServiceName());
		buffer.append("\nSource id:" + this.getSourceId());
		buffer.append("\nStatus:" + this.getStatus());
		buffer.append("\nStyle:" + this.getStylePopMethodId());
		buffer.append("\nUpdated by:" + this.getUpdatedBy());
		buffer.append("\nUpdated date:" + this.getUpdatedDate());
		buffer.append("\nVendor number:" + this.getVendorNumber());
		buffer.append("\nVendor name:" + this.getVendorName());
		buffer.append("\nVendor number:" + this.getVendorNumber());
		buffer.append("\nRequest status:" + this.getRequestStatus());
		buffer.append("\nShow table:" + this.getShowTable());
		buffer.append("\nUploaded styles:" + this.getUploadedStyles());
		buffer.append("\nFailed styles:" + this.getFailedStyles());
		buffer.append("\nPosted date:" + this.getPostedDate());
		buffer.append("\nPosted by:" + this.getPostedBy());
		buffer.append("\nValid styles:" + this.getValidStyles());
		buffer.append("\nReject Reason:" + this.getRejectReason());
		buffer.append("\n----------------------------------------------------------");
		return buffer.toString();
	}*/

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the source
	 */
	public String getSourceId() {
		return sourceId;
	}

	/**
	 * @param source the source to set
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	

	/**
	 * @return the merchandise
	 */
	public String getMerchandise() {
		return merchandise;
	}

	/**
	 * @param merchandise the merchandise to set
	 */
	public void setMerchandise(String merchandise) {
		this.merchandise = merchandise;
	}

	/**
	 * @return the minimumMarkup
	 */
	public String getMinimumMarkup() {
		return minimumMarkup;
	}

	/**
	 * @param minimumMarkup the minimumMarkup to set
	 */
	public void setMinimumMarkup(String minimumMarkup) {
		this.minimumMarkup = minimumMarkup;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}


	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @param userdepts the userdepts to set
	 */
	public void setUserdepts(Boolean userdepts) {
		this.userdepts = userdepts;
	}

	/**
	 * @return the userdepts
	 */
	public Boolean getUserdepts() {
		return userdepts;
	}

	/**
	 * @param stylePopMethodId the stylePopMethodId to set
	 */
	public void setStylePopMethodId(String stylePopMethodId) {
		this.stylePopMethodId = stylePopMethodId;
	}

	/**
	 * @return the stylePopMethodId
	 */
	public String getStylePopMethodId() {
		return stylePopMethodId;
	}

	/**
	 * @return the file
	 */
	public byte[] getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(byte[] file) {
		this.file = file;
	}

	/**
	 * @param exceptionReason the exceptionReason to set
	 */
	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	/**
	 * @return the exceptionReason
	 */
	public String getExceptionReason() {
		return exceptionReason;
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
	 * @return the vendorNumber
	 */
	public String getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * @param vendorNumber the vendorNumber to set
	 */
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
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
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param requestStatus the requestStatus to set
	 */
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	/**
	 * @return the requestStatus
	 */
	public String getRequestStatus() {
		return requestStatus;
	}

	/**
	 * @param postedBy the postedBy to set
	 */
	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	/**
	 * @return the postedBy
	 */
	public String getPostedBy() {
		return postedBy;
	}

	/**
	 * @param postedDate the postedDate to set
	 */
	public void setPostedDate(String postedDate) {
		this.postedDate = postedDate;
	}

	/**
	 * @return the postedDate
	 */
	public String getPostedDate() {
		return postedDate;
	}

	/**
	 * @param itemAction the itemAction to set
	 */
	public void setItemAction(String itemAction) {
		this.itemAction = itemAction;
	}

	/**
	 * @return the itemAction
	 */
	public String getItemAction() {
		return itemAction;
	}

	/**
	 * @param showTable the showTable to set
	 */
	public void setShowTable(String showTable) {
		this.showTable = showTable;
	}

	/**
	 * @return the showTable
	 */
	public String getShowTable() {
		return showTable;
	}

	/**
	 * @param uploadedStyles the uploadedStyles to set
	 */
	public void setUploadedStyles(String uploadedStyles) {
		this.uploadedStyles = uploadedStyles;
	}

	/**
	 * @return the uploadedStyles
	 */
	public String getUploadedStyles() {
		return uploadedStyles;
	}

	/**
	 * @param failedStyles the failedStyles to set
	 */
	public void setFailedStyles(String failedStyles) {
		this.failedStyles = failedStyles;
	}

	/**
	 * @return the failedStyles
	 */
	public String getFailedStyles() {
		return failedStyles;
	}

	/**
	 * @param validStyles the validStyles to set
	 */
	public void setValidStyles(String validStyles) {
		this.validStyles = validStyles;
	}

	/**
	 * @return the validStyles
	 */
	public String getValidStyles() {
		return validStyles;
	}

	/**
	 * @param rejectReason the rejectReason to set
	 */
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	/**
	 * @return the rejectReason
	 */
	public String getRejectReason() {
		return rejectReason;
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
	 * @param styleDetails the styleDetails to set
	 */
	public void setStyleDetails(List<VFIRStyle> styleDetails) {
		this.styleDetails = styleDetails;
	}

	/**
	 * @return the styleDetails
	 */
	public List<VFIRStyle> getStyleDetails() {
		return styleDetails;
	}

	/**
	 * @param skuDetails the skuDetails to set
	 */
	public void setSkuDetails(List<StylesDTO> skuDetails) {
		this.skuDetails = skuDetails;
	}

	/**
	 * @return the skuDetails
	 */
	public List<StylesDTO> getSkuDetails() {
		return skuDetails;
	}

	/**
	 * @param updateDetails the updateDetails to set
	 */
	public void setUpdateDetails(List<ItemRequestWorkflow> updateDetails) {
		this.updateDetails = updateDetails;
	}

	/**
	 * @return the updateDetails
	 */
	public List<ItemRequestWorkflow> getUpdateDetails() {
		return updateDetails;
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
	 * @param selectedStyles the selectedStyles to set
	 */
	public void setSelectedStyles(List<String> selectedStyles) {
		this.selectedStyles = selectedStyles;
	}

	/**
	 * @return the selectedStyles
	 */
	public List<String> getSelectedStyles() {
		return selectedStyles;
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

	/**
	 * @param enableDisableSkuException the enableDisableSkuException to set
	 */
	public void setEnableDisableSkuException(Boolean enableDisableSkuException) {
		this.enableDisableSkuException = enableDisableSkuException;
	}

	/**
	 * @return the enableDisableSkuException
	 */
	public Boolean getEnableDisableSkuException() {
		return enableDisableSkuException;
	}

	/**
	 * @param approveRole the approveRole to set
	 */
	public void setApproveRole(String approveRole) {
		this.approveRole = approveRole;
	}

	/**
	 * @return the approveRole
	 */
	public String getApproveRole() {
		return approveRole;
	}

	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return the pageNo
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * @param totalResultSize the totalResultSize to set
	 */
	public void setTotalResultSize(Integer totalResultSize) {
		this.totalResultSize = totalResultSize;
	}

	/**
	 * @return the totalResultSize
	 */
	public Integer getTotalResultSize() {
		return totalResultSize;
	}

}
