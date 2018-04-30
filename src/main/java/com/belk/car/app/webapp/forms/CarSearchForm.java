package com.belk.car.app.webapp.forms;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;

import com.belk.car.app.model.Status;


public class CarSearchForm {
	
	private String carId;
	private String workflowStatus;
	private String currentUserType ;

	private String dueDateFrom;
	private String dueDateTo;
	private String deptCd;  //join
	private String vendorStyleNumber; //join vendor_style
	private String parentVendorStyleNumber;//Added for performance improvement
	private String vendorNumber; //join vendor_number
	private String vendorName ;
	private String classNumber; // join classification
	private User user;
	private boolean ignoreUser ;
	private String sourceTypeCd ;
	private String sourceId ;
	private String statusCd ;
	private boolean ignoreClosedCars ;
	private String contentStatus ;
	private boolean searchChildVendorStyle = true ;
	private String createDate ;
	private String updateDate ;
	private String productType;
	private String belkUPC;
	private Boolean archive;
	private String promotionType;
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getContentStatus() {
		return contentStatus;
	}

	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}

	/**
	 * @return the carId
	 */
	public String getCarId() {
		return carId;
	}
	/**
	 * @param carId the carId to set
	 */
	public void setCarId(String carId) {
		this.carId = carId;
	}
	/**
	 * @return the deptCd
	 */
	public String getDeptCd() {
		return deptCd;
	}
	/**
	 * @param deptCd the deptCd to set
	 */
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	/**
	 * @return the vendorStyleNumber
	 */
	public String getVendorStyleNumber() {
		return vendorStyleNumber;
	}
	/**
	 * @param vendorStyleNumber the vendorStyleNumber to set
	 */
	public void setVendorStyleNumber(String vendorStyleNumber) {
		this.vendorStyleNumber = stripSpecialChars(vendorStyleNumber);
	}
	/**
	 * @return the vendorStyleNumber
	 */
	public String getParentVendorStyleNumber() {
		return parentVendorStyleNumber;
	}
	/**
	 * @param vendorStyleNumber the vendorStyleNumber to set
	 */
	public void setParentVendorStyleNumber(String parentVendorStyleNumber) {
		this.parentVendorStyleNumber = stripSpecialChars(parentVendorStyleNumber);
	}
	/**
	 * @return the classNumber
	 */
	public String getClassNumber() {
		return classNumber;
	}
	/**
	 * @param classNumber the classNumber to set
	 */
	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}
	/**
	 * @return the workflowStatus
	 */
	public String getWorkflowStatus() {
		return workflowStatus;
	}
	/**
	 * @param workflowStatus the workflowStatus to set
	 */
	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	
	/**
	 * @return the dueDateFrom
	 */
	public String getDueDateFrom() {
		return dueDateFrom;
	}

	/**
	 * @param dueDateFrom the dueDateFrom to set
	 */
	public void setDueDateFrom(String dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	/**
	 * @return the dueDateTo
	 */
	public String getDueDateTo() {
		return dueDateTo;
	}

	/**
	 * @param dueDateTo the dueDateTo to set
	 */
	public void setDueDateTo(String dueDateTo) {
		this.dueDateTo = dueDateTo;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = stripSpecialChars(vendorNumber);
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = stripSpecialChars(vendorName);
	}

	public String getSourceTypeCd() {
		return sourceTypeCd;
	}

	public void setSourceTypeCd(String sourceTypeCd) {
		this.sourceTypeCd = sourceTypeCd;
	}

	public String getSourceId() {
		return sourceId;
	}
	
	public boolean isIgnoreUser() {
		return ignoreUser;
	}
	
	public void setIgnoreUser(boolean ignoreUser) {
		this.ignoreUser = ignoreUser;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getStatusCd() {
		if (StringUtils.isBlank(statusCd))
			statusCd = Status.ACTIVE ;
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public boolean isIgnoreClosedCars() {
		return ignoreClosedCars;
	}

	public void setIgnoreClosedCars(boolean ignoreClosedCars) {
		this.ignoreClosedCars = ignoreClosedCars;
	}

	public String getCurrentUserType() {
		return currentUserType;
	}

	public void setCurrentUserType(String currentUserType) {
		this.currentUserType = currentUserType;
	}

	public boolean alsoSearchChildVendorStyle() {
		return searchChildVendorStyle;
	}

	public void setSearchChildVendorStyle(boolean searchChildVendorStyle) {
		this.searchChildVendorStyle = searchChildVendorStyle;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public void setProductType(String productType) {
		this.productType = stripSpecialChars(productType);
	}

	public String getProductType() {
		return productType;
	}

	public void setBelkUPC(String belkUPC) {
		this.belkUPC = stripSpecialChars(belkUPC);
	}

	public String getBelkUPC() {
		return belkUPC;
	}

	/**
	 * @param archive the archive to set
	 */
	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	/**
	 * @return the archive
	 */
	public Boolean getArchive() {
		return archive;
	}
	
	public String stripSpecialChars(String str){
		if(null != str){
			str = str.replaceAll("[^\\p{Graph},^\\p{Blank}]", "").trim();
		}
		return str;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

}
