package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.List;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author afusyq3
 *
 */
public class UploadImagesDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> imageLocationType;
	private List<String> imageType;
	private List<String> shotTypeValue;
	private String cdImageLocation;
	private CommonsMultipartFile  cdImageFile;
	private String ftpUrl;
	private String ftpPath;
	private String ftpFileName;
	private String ftpUserId;
	private String ftpPassword;
	private String ftpImageName;
	private String vendorNumber;
	private String styleNumber;
	private String colorCode;
	private String colorName;
	private String shotType;
	private String imageName;
	private Long vendorStyleId;
	private Long CarId;
	private String vendorImageUploadDir;
	private String RRDImageUploadedDir;
	private String userUploadedFileName;
	private long pimImageId;
	private String styleColor;
	

	/**
	 * @return the userUploadedFileName
	 */
	public String getUserUploadedFileName() {
		return userUploadedFileName;
	}
	/**
	 * @param userUploadedFileName the userUploadedFileName to set
	 */
	public void setUserUploadedFileName(String userUploadedFileName) {
		this.userUploadedFileName = userUploadedFileName;
	}
	/**
	 * @return the rRDImageUploadedDir
	 */
	public String getRRDImageUploadedDir() {
		return RRDImageUploadedDir;
	}
	/**
	 * @param rRDImageUploadedDir the rRDImageUploadedDir to set
	 */
	public void setRRDImageUploadedDir(String rRDImageUploadedDir) {
		RRDImageUploadedDir = rRDImageUploadedDir;
	}
	/**
	 * @return the vendorImageUploadDir
	 */
	public String getVendorImageUploadDir() {
		return vendorImageUploadDir;
	}
	/**
	 * @param vendorImageUploadDir the vendorImageUploadDir to set
	 */
	public void setVendorImageUploadDir(String vendorImageUploadDir) {
		this.vendorImageUploadDir = vendorImageUploadDir;
	}
	/**
	 * @return the carId
	 */
	public Long getCarId() {
		return CarId;
	}
	/**
	 * @param carId the carId to set
	 */
	public void setCarId(Long carId) {
		CarId = carId;
	}
	/**
	 * @return the imageLocationType
	 */
	public List<String> getImageLocationType() {
		return imageLocationType;
	}
	/**
	 * @param imageLocationType the imageLocationType to set
	 */
	public void setImageLocationType(List<String> imageLocationType) {
		this.imageLocationType = imageLocationType;
	}
	/**
	 * @return the imageType
	 */
	public List<String> getImageType() {
		return imageType;
	}
	/**
	 * @param imageType the imageType to set
	 */
	public void setImageType(List<String> imageType) {
		this.imageType = imageType;
	}
	/**
	 * @return the cdImageLocation
	 */
	public String getCdImageLocation() {
		return cdImageLocation;
	}
	/**
	 * @param cdImageLocation the cdImageLocation to set
	 */
	public void setCdImageLocation(String cdImageLocation) {
		this.cdImageLocation = cdImageLocation;
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
	 * @return the ftpPath
	 */
	public String getFtpPath() {
		return ftpPath;
	}
	/**
	 * @param ftpPath the ftpPath to set
	 */
	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}
	/**
	 * @return the ftpFileName
	 */
	public String getFtpFileName() {
		return ftpFileName;
	}
	/**
	 * @param ftpFileName the ftpFileName to set
	 */
	public void setFtpFileName(String ftpFileName) {
		this.ftpFileName = ftpFileName;
	}
	/**
	 * @return the ftpUserId
	 */
	public String getFtpUserId() {
		return ftpUserId;
	}
	/**
	 * @param ftpUserId the ftpUserId to set
	 */
	public void setFtpUserId(String ftpUserId) {
		this.ftpUserId = ftpUserId;
	}
	/**
	 * @return the ftpPassword
	 */
	public String getFtpPassword() {
		return ftpPassword;
	}
	/**
	 * @param ftpPassword the ftpPassword to set
	 */
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	/**
	 * @return the ftpImageName
	 */
	public String getFtpImageName() {
		return ftpImageName;
	}
	/**
	 * @param ftpImageName the ftpImageName to set
	 */
	public void setFtpImageName(String ftpImageName) {
		this.ftpImageName = ftpImageName;
	}
	/**
	 * @return the imageTypeTest
	 */
	/**
	 * @return the cdImageFile
	 */
	public CommonsMultipartFile getCdImageFile() {
		return cdImageFile;
	}
	/**
	 * @param cdImageFile the cdImageFile to set
	 */
	public void setCdImageFile(CommonsMultipartFile cdImageFile) {
		this.cdImageFile = cdImageFile;
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
	 * @return the styleNumber
	 */
	public String getStyleNumber() {
		return styleNumber;
	}
	/**
	 * @param styleNumber the styleNumber to set
	 */
	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}
	/**
	 * @return the colorCode
	 */
	public String getColorCode() {
		return colorCode;
	}
	/**
	 * @param colorCode the colorCode to set
	 */
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	/**
	 * @return the colorName
	 */
	public String getColorName() {
		return colorName;
	}
	/**
	 * @param colorName the colorName to set
	 */
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	/**
	 * @return the shotType
	 */
	public String getShotType() {
		return shotType;
	}
	/**
	 * @param shotType the shotType to set
	 */
	public void setShotType(String shotType) {
		this.shotType = shotType;
	}
	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}
	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	/**
	 * @return the vendorStyleId
	 */
	public Long getVendorStyleId() {
		return vendorStyleId;
	}
	/**
	 * @param vendorStyleId the vendorStyleId to set
	 */
	public void setVendorStyleId(Long vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}
	public List<String> getShotTypeValue() {
		return shotTypeValue;
	}
	public void setShotTypeValue(List<String> shotTypeValue) {
		this.shotTypeValue = shotTypeValue;
	}
	public long getPimImageId() {
		return pimImageId;
	}
	public void setPimImageId(long pimImageId) {
		this.pimImageId = pimImageId;
	}
	public String getStyleColor() {
		return styleColor;
	}
	public void setStyleColor(String styleColor) {
		this.styleColor = styleColor;
	}
	
}
