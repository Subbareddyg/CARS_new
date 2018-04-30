/**
 * 
 */
package com.belk.car.app.dto;

/**
 * @author afusy01
 *
 */
public class StyleDetailsDTO {
	private String styleNumber;
	private String styleDescription;
	private String dropShipStatus;
	private Integer noOfDropShipSkus;
	private String idbAllowDropship;
	private Integer noOfIdbDropshipSkus;
	
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
	 * @return the styleDescription
	 */
	public String getStyleDescription() {
		return styleDescription;
	}
	/**
	 * @param styleDescription the styleDescription to set
	 */
	public void setStyleDescription(String styleDescription) {
		this.styleDescription = styleDescription;
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
	 * @return the noOfDropShipSkus
	 */
	public Integer getNoOfDropShipSkus() {
		return noOfDropShipSkus;
	}
	/**
	 * @param noOfDropShipSkus the noOfDropShipSkus to set
	 */
	public void setNoOfDropShipSkus(Integer noOfDropShipSkus) {
		this.noOfDropShipSkus = noOfDropShipSkus;
	}
	/**
	 * @return the idbAllowDropship
	 */
	public String getIdbAllowDropship() {
		return idbAllowDropship;
	}
	/**
	 * @param idbAllowDropship the idbAllowDropship to set
	 */
	public void setIdbAllowDropship(String idbAllowDropship) {
		this.idbAllowDropship = idbAllowDropship;
	}
	/**
	 * @return the noOfIdbDropshipSkus
	 */
	public Integer getNoOfIdbDropshipSkus() {
		return noOfIdbDropshipSkus;
	}
	/**
	 * @param noOfIdbDropshipSkus the noOfIdbDropshipSkus to set
	 */
	public void setNoOfIdbDropshipSkus(Integer noOfIdbDropshipSkus) {
		this.noOfIdbDropshipSkus = noOfIdbDropshipSkus;
	}
}
