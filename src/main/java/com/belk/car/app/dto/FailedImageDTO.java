/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.List;
import com.belk.car.app.model.Department;

/**
 * @author afurxd2
 *
 */
public class FailedImageDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long carId;
	String expectedShipDate;
	String vendorStyleNo;
	String colorCode;
	String imageName;
	String originalName;
	String failureType;
	String failureReasons;
	

	/**
	 * @return the originalName
	 */
	public String getOriginalName() {
		return originalName;
	}
	/**
	 * @param originalName the originalName to set
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	/**
	 * @return the failureReasons
	 */
	public String getFailureReasons() {
		return failureReasons;
	}
	/**
	 * @param failureReasons the failureReasons to set
	 */
	public void setFailureReasons(String failureReasons) {
		this.failureReasons = failureReasons;
	}
	/**
	 * @return the carId
	 */
	public long getCarId() {
		return carId;
	}
	/**
	 * @param carId the carId to set
	 */
	public void setCarId(long carId) {
		this.carId = carId;
	}
	/**
	 * @return the expectedShipDate
	 */
	public String getExpectedShipDate() {
		return expectedShipDate;
	}
	/**
	 * @param expectedShipDate the expectedShipDate to set
	 */
	public void setExpectedShipDate(String expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}
	/**
	 * @return the vendorStyleNo
	 */
	public String getVendorStyleNo() {
		return vendorStyleNo;
	}
	/**
	 * @param vendorStyleNo the vendorStyleNo to set
	 */
	public void setVendorStyleNo(String vendorStyleNo) {
		this.vendorStyleNo = vendorStyleNo;
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
	 * @return the failureType
	 */
	public String getFailureType() {
		return failureType;
	}
	/**
	 * @param failureType the failureType to set
	 */
	public void setFailureType(String failureType) {
		this.failureType = failureType;
	}
	
	
	
	
}
