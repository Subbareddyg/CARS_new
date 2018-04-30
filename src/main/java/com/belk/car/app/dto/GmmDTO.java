/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;

/**
 * @author Syntel
 *
 */
public class GmmDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3571812828568431013L;

	
	private String gmmName;
	private Integer lateCarsCount;
	private Integer skuCount;
	
	public GmmDTO(){
		gmmName = "";
		lateCarsCount =0;
		skuCount =0;
	}
	/**
	 * @return the gmmName
	 */
	public String getGmmName() {
		return gmmName;
	}
	/**
	 * @param gmmName the gmmName to set
	 */
	public void setGmmName(String gmmName) {
		this.gmmName = gmmName;
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
