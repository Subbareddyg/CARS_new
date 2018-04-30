package com.belk.car.app.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;

public class CarSearchCriteria implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4594896116722654234L;

	private String carId;
	private String workflowStatus;
	private String currentUserType ;

	private String dueDateFrom;
	private String dueDateTo;
	private String deptCd;  //join
	private String vendorStyleStatusCd;
	private String vendorStyleNumber; //join vendor_style
	private String parentVendorStyleNumber; //join parent vendor_style
	private String vendorNumber; //join vendor
	private String vendorName; //join vendor
	private String classNumber; // join classification
	private String vendorUpc ;
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
	private boolean notUpdatedSince ;
	private boolean requestedImages = false ;
	private boolean recievedImages = false ;
	private boolean fromSearch = false ;
	private boolean includeOutFitCars = false;
	private boolean readyToSendToCMP = false ; 
	private boolean readyToClose = false ; 
	private String expShipDateFrom;
	private String expShipDateTo;
	private boolean isCopyCar = false ; 
	private String productType;
	private String belkUPC;
	private Boolean archive;
	private String enableArchive;
	private String enableDelete;
	private String reportName;
	private String sortColumnNm;
	private String sortOrder;
	private String currentPage;
	private String promotionType;
	private boolean isFromDashBoardAndSearchCar = false ;
        private List<String> statusCriteria;

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}
	
	private boolean includePYGCars=false;
	
	private boolean includePromoType =false;
	
	public boolean isIncludePromoType() {
		return includePromoType;
	}

	public void setIncludePromoType(boolean includePromoType) {
		this.includePromoType = includePromoType;
	}

	public boolean isIncludePYGCars() {
		return includePYGCars;
	}

	public void setIncludePYGCars(boolean includePYGCars) {
		this.includePYGCars = includePYGCars;
	}

	/**
	 * Set this value when trying to retrieve data for Export to CMP
	 * Will retrieve Car with Content Status of COMPLETE or RESEND
	 * @return
	 */
	public boolean isReadyToSendToCMP() {
		return readyToSendToCMP;
	}

	public void setReadyToSendToCMP(boolean readyToSendToCMP) {
		this.readyToSendToCMP = readyToSendToCMP;
	}

	/**
	 * Set this value when using the SearchForm
	 * Allow the Query to take a different path when performing a Search (eg: BUYER is allowed to search CAR assigned to WEBMERCHANTS)
	 * @return
	 */
	public boolean isFromSearch() {
		return fromSearch;
	}

	public void setFromSearch(boolean fromSearch) {
		this.fromSearch = fromSearch;
	}

	public boolean hasRequestedImages() {
		return requestedImages;
	}

	public void setRequestedImages(boolean requestedImages) {
		this.requestedImages = requestedImages;
	}

	public boolean hasRecievedImages() {
		return recievedImages;
	}

	public void setRecievedImages(boolean recievedImages) {
		this.recievedImages = recievedImages;
	}

	public boolean isNotUpdatedSince() {
		return notUpdatedSince;
	}

	public void setNotUpdatedSince(boolean notUpdatedSince) {
		this.notUpdatedSince = notUpdatedSince;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = trimValue(createDate);
	}

	public String getContentStatus() {
		return contentStatus;
	}

	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}

	public CarSearchCriteria() 
	{
		super();
	}

	/**
	 * @param user
	 */
	public CarSearchCriteria(User user) {
		super();
		this.user = user;
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
		this.carId = trimValue(carId);
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
		this.deptCd = trimValue(deptCd);
	}
	/**
     * @return the vendorStyleStatusCd
     */
    public String getVendorStyleStatusCd() {
        return vendorStyleStatusCd;
    }
    /**
     * @param vendorStyleStatusCd the vendorStyleStatusCd to set
     */
    public void setVendorStyleStatusCd(String vendorStyleStatusCd) {
        
        this.vendorStyleStatusCd = vendorStyleStatusCd;
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
		
		this.vendorStyleNumber = trimValue(vendorStyleNumber);
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
		this.classNumber = trimValue(classNumber);
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
		this.dueDateFrom = trimValue(dueDateFrom);
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
		this.dueDateTo = trimValue(dueDateTo);
	}

	private String trimValue(String dueDateTo) {
		if(null!=dueDateTo)
		   return dueDateTo.trim();
		else
			return null;
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
		this.vendorNumber = trimValue(vendorNumber);
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {		
		this.vendorName = trimValue(vendorName);
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
		this.updateDate = trimValue(updateDate);
	}

	public String getVendorUpc() {
		return vendorUpc;
	}

	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}

	public String getExpShipDateFrom() {
		return expShipDateFrom;
	}

	public void setExpShipDateFrom(String expShipDateFrom) {
		this.expShipDateFrom = expShipDateFrom;
	}

	public String getExpShipDateTo() {
		return expShipDateTo;
	}

	public void setExpShipDateTo(String expShipDateTo) {
		this.expShipDateTo = expShipDateTo;
	}

	public boolean isReadyToClose() {
		return readyToClose;
	}

	public void setReadyToClose(boolean readyToClose) {
		this.readyToClose = readyToClose;
	}
	public void setProductType(String productType) {		
		this.productType = trimValue(productType);
	}

	public String getProductType() {
		return productType;
	}

	public void setBelkUPC(String belkUPC) {		
		this.belkUPC = trimValue(belkUPC);;
	}

	public String getBelkUPC() {
		return belkUPC;
	}
	
	public String toString() {
		StringBuffer strB = new StringBuffer();
		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = this.getClass().getDeclaredFields();
		Set<String> donotIncludeFields = new HashSet<String>() ;
		donotIncludeFields.add("serialVersionUID");
		donotIncludeFields.add("user");
		donotIncludeFields.add("ignoreUser");
		donotIncludeFields.add("ignoreClosedCars");
		donotIncludeFields.add("searchChildVendorStyle");
		donotIncludeFields.add("notUpdatedSince");
		donotIncludeFields.add("requestedImages");
		donotIncludeFields.add("fromSearch");
		donotIncludeFields.add("readyToSendToCMP");
		donotIncludeFields.add("readyToClose");
		donotIncludeFields.add("recievedImages");

		//print field names paired with their values
		for (Field field : fields) {
			//strB.append("  ");
			try {
				if (!donotIncludeFields.contains(field.getName())) {
					Object obj = field.get(this);
					if (obj != null && StringUtils.isNotBlank(obj.toString())) {
						strB.append(field.getName());
						strB.append(": ");
						// requires access to private field:
						strB.append(obj.toString());
	
						strB.append("; ");
					}
				}
			} catch (IllegalAccessException ex) {
				//System.out.println(ex);
			}
		}
		return strB.toString();
	}

	/**
	 * @param isCopyCar the isCopyCar to set
	 */
	public void setCopyCar(boolean isCopyCar) {
		this.isCopyCar = isCopyCar;
	}

	/**
	 * @return the isCopyCar
	 */
	public boolean isCopyCar() {
		return isCopyCar;
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

	/**
	 * @param enableArchive the enableArchive to set
	 */
	public void setEnableArchive(String enableArchive) {
		this.enableArchive = enableArchive;
	}

	/**
	 * @return the enableArchive
	 */
	public String getEnableArchive() {
		return enableArchive;
	}

	/**
	 * @param enableDelete the enableDelete to set
	 */
	public void setEnableDelete(String enableDelete) {
		this.enableDelete = enableDelete;
	}

	/**
	 * @return the enableDelete
	 */
	public String getEnableDelete() {
		return enableDelete;
	}

	public boolean isIncludeOutFitCars() {
		return includeOutFitCars;
	}

	public void setIncludeOutFitCars(boolean includeOutFitCars) {
		this.includeOutFitCars = includeOutFitCars;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getSortColumnNm() {
		return sortColumnNm;
	}

	public void setSortColumnNm(String sortColumnNm) {
		this.sortColumnNm = sortColumnNm;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getParentVendorStyleNumber() {
		return parentVendorStyleNumber;
	}

	public void setParentVendorStyleNumber(String parentVendorStyleNumber) {
		this.parentVendorStyleNumber = parentVendorStyleNumber;
	}
	public boolean getIsFromDashBoardAndSearchCar() {
		return isFromDashBoardAndSearchCar;
	}

	public void setIsFromDashBoardAndSearchCar(boolean isFromDashBoardAndSearchCar) {
		this.isFromDashBoardAndSearchCar = isFromDashBoardAndSearchCar;
	}

        public void setContentStatusList(List<String> statusCriteria) {
            this.statusCriteria = statusCriteria;
        }
        
        public List<String> getContentStatusList() {
            return this.statusCriteria;
        }
}
