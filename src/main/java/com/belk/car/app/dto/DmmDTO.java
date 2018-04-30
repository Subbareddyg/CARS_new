/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;

/**
 * @author Syntel
 *
 */
public class DmmDTO implements Serializable {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 509429990081639843L;
	
	
	private String dmmName;
	private Integer lateCarsCount;
	private Integer skuCount;
	
	public DmmDTO(){
		dmmName = "";
		lateCarsCount =0;
		skuCount =0;
	}
	
	//You can add here List<UserDTO>
	
	/**
	 * @return the dmmName
	 */
	public String getDmmName() {
		return dmmName;
	}
	/**
	 * @param dmmName the dmmName to set
	 */
	public void setDmmName(String dmmName) {
		this.dmmName = dmmName;
	}
	/**
	 * @return the lateCarsCount
	 */
	public Integer getLateCarsCount() {
		return lateCarsCount;
	}
	/**
	 * @param lateCarsCount the lateCarsCount to set
	 */
	public void setLateCarsCount(Integer lateCarsCount) {
		this.lateCarsCount = lateCarsCount;
	}
	/**
	 * @return the skuCount
	 */
	public Integer getSkuCount() {
		return skuCount;
	}
	/**
	 * @param skuCount the skuCount to set
	 */
	public void setSkuCount(Integer skuCount) {
		this.skuCount = skuCount;
	}


}
