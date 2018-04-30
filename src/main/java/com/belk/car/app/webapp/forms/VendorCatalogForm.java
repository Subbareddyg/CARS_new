/**
 * 
 */
package com.belk.car.app.webapp.forms;

import java.util.List;

import org.appfuse.model.User;

import com.belk.car.app.model.Department;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;

/**
 * This class contains form fields mapped with vendor catalog setup form
 * @author afusya2
 */
public class VendorCatalogForm extends VendorCatalog implements java.io.Serializable {
	private static final long serialVersionUID = 6099774321313615467L;
	
	public VendorCatalog vendorCatalog;
	public VendorCatalogImport vendorCatalogImport;
	
	private String vendorNumber;
	private String vendorName;
	private String catalogName;
	private String catalogId;
	private List<Department> departments;
	private User user;
	private byte[] fileName;
	private String filePath;
	private String fileFormat;
	private String delimeter;	
	private String uploadImage;	
	private String imgLocn;
	private byte[] cdLocan;
	private String ftpUrl;
	private String previousCatalogID; 
	private List<VendorCatalogImport> previousVendorCatalogs;
	
	private Boolean isAnonymousFTP;
	private String ftpUname;
	private String ftpPwd;
	private String updateAction;
	private String buttonClicked;
	private boolean error;

	public VendorCatalogForm(){
		vendorCatalog = new VendorCatalog();
		vendorCatalogImport = new VendorCatalogImport();
		
		//previousVendorCatalogs.add(previousVendorCatalogObject);
	}
	
	
	/**
	 * @return the vendorCatalog
	 */
	public VendorCatalog getVendorCatalog() {
		return vendorCatalog;
	}


	/**
	 * @param vendorCatalog the vendorCatalog to set
	 */
	public void setVendorCatalog(VendorCatalog vendorCatalog) {
		this.vendorCatalog = vendorCatalog;
	}


	/**
	 * @return the vendorCatalogImport
	 */
	public VendorCatalogImport getVendorCatalogImport() {
		return vendorCatalogImport;
	}


	/**
	 * @param vendorCatalogImport the vendorCatalogImport to set
	 */
	public void setVendorCatalogImport(VendorCatalogImport vendorCatalogImport) {
		this.vendorCatalogImport = vendorCatalogImport;
	}


	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n----------------------------------------------------------");
		buffer.append("\nEffective date:" + this.getVendorNumber());
		buffer.append("\nAction:" + this.getVendorName());
		buffer.append("\nCreated By:" + this.getCatalogName());
		//buffer.append("\nCreated date:" + this.getDepartments());
		buffer.append("\nDescription:" + this.getFileName());
		buffer.append("\nException reasons:" + this.getFilePath());
		buffer.append("\nFile path:" + this.getFileFormat());
		buffer.append("\nPrevious request id:" + this.getDelimeter());
		buffer.append("\nOption:" + this.getUploadImage());
		buffer.append("\nPercentage:" + this.getImgLocn());
		buffer.append("\nFulfillment service id:" + this.getFtpUrl());
		buffer.append("\nFulfillment service name:" + this.getFtpUname());
		buffer.append("\nSource id:" + this.getFtpPwd());
		buffer.append("\nStatus:" + this.getDepartments().size());
		buffer.append("\nStatus:" + this.getUser());
		
		return buffer.toString();
	}
	
	public String getVendorNumber() {
		return vendorNumber;
	}
	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}
	
	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}
	public String getUploadImage() {
		return uploadImage;
	}
	public void setUploadImage(String uploadImage) {
		this.uploadImage = uploadImage;
	}
	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	/**
	 * @return the catalogName
	 */
	public String getCatalogName() {
		return catalogName;
	}
	/**
	 * @param catalogName the catalogName to set
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	
	/**
	 * @return the catalogId
	 */
	public String getCatalogId() {
		return catalogId;
	}

	/**
	 * @param catalogId the catalogId to set
	 */
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	/**
	 * @return the departments
	 */
	public List<Department> getDepartments() {
		return departments;
	}

	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
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

	/**
	 * @return the fileName
	 */
	public byte[] getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(byte[] fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the fileFormat
	 */
	public String getFileFormat() {
		return fileFormat;
	}
	/**
	 * @param fileFormat the fileFormat to set
	 */
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	/**
	 * @return the delimeter
	 */
	public String getDelimeter() {
		return delimeter;
	}
	/**
	 * @param delimeter the delimeter to set
	 */
	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}
	/**
	 * @return the imgLocn
	 */
	public String getImgLocn() {
		return imgLocn;
	}
	/**
	 * @param imgLocn the imgLocn to set
	 */
	public void setImgLocn(String imgLocn) {
		this.imgLocn = imgLocn;
	}
	
	/**
	 * @return the cdLocan
	 */
	public byte[] getCdLocan() {
		return cdLocan;
	}
	/**
	 * @param cdLocan the cdLocan to set
	 */
	public void setCdLocan(byte[] cdLocan) {
		this.cdLocan = cdLocan;
	}
	/**
	 * @return the ftpUrl
	 */
	public String getFtpUrl() {
		return ftpUrl;
	}
	/**
	 * @param ftpUrl the ftpUrl to set
	 */
	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}
	
	
	

	/**
	 * @return the previousCatalogID
	 */
	public String getPreviousCatalogID() {
		return previousCatalogID;
	}


	/**
	 * @param previousCatalogID the previousCatalogID to set
	 */
	public void setPreviousCatalogID(String previousCatalogID) {
		this.previousCatalogID = previousCatalogID;
	}


	public Boolean getIsAnonymousFTP() {
		return isAnonymousFTP;
	}
	public void setIsAnonymousFTP(Boolean isAnonymousFTP) {
		this.isAnonymousFTP = isAnonymousFTP;
	}
	/**
	 * @return the ftpUname
	 */
	public String getFtpUname() {
		return ftpUname;
	}
	/**
	 * @param ftpUname the ftpUname to set
	 */
	public void setFtpUname(String ftpUname) {
		this.ftpUname = ftpUname;
	}
	/**
	 * @return the ftpPwd
	 */
	public String getFtpPwd() {
		return ftpPwd;
	}
	/**
	 * @param ftpPwd the ftpPwd to set
	 */
	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}
	/**
	 * @return the updateAction
	 */
	public String getUpdateAction() {
		return updateAction;
	}
	/**
	 * @param updateAction the updateAction to set
	 */
	public void setUpdateAction(String updateAction) {
		this.updateAction = updateAction;
	}
	/**
	 * @return the buttonClicked
	 */
	public String getButtonClicked() {
		return buttonClicked;
	}

	/**
	 * @param buttonClicked the buttonClicked to set
	 */
	public void setButtonClicked(String buttonClicked) {
		this.buttonClicked = buttonClicked;
	}


	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}


	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}


	/**
	 * @param previousVendorCatalogs the previousVendorCatalogs to set
	 */
	public void setPreviousVendorCatalogs(List<VendorCatalogImport> previousVendorCatalogs) {
		this.previousVendorCatalogs = previousVendorCatalogs;
	}


	/**
	 * @return the previousVendorCatalogs
	 */
	public List<VendorCatalogImport> getPreviousVendorCatalogs() {
		return previousVendorCatalogs;
	}


	

}
