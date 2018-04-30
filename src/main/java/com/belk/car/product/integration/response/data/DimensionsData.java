package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the dimension information. Usually this will be a collection object
 * under the entry section in the response. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry/Item_Ctg_Spec/Supplier/Country/.
 */
@XmlRootElement(name = "Dimensions")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DimensionsData {
	
	private String dimensionCode;
	
	private String objectName;
	
	private String tareWeight;
	
	private String tareType;
	
	private String lwhUOM;
	
	private String length;
	
	private String width;
	
	private String height;
	
	private String liquidVolumeUOM;
	
	private String liquidVolume;
	
	private String statisticsCube;
	
	private String weightUOM;
	
	private String weight;
	
	private String netWeight;
	
	private String presentationMethod;

	@XmlAttribute(name = "occ")
	public String getDimensionCode() {
		return dimensionCode;
	}

	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}

	@XmlElement(name = "Object")
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@XmlElement(name = "Tare_Weight")
	public String getTareWeight() {
		return tareWeight;
	}

	public void setTareWeight(String tareWeight) {
		this.tareWeight = tareWeight;
	}

	@XmlElement(name = "Tare_Type")
	public String getTareType() {
		return tareType;
	}

	public void setTareType(String tareType) {
		this.tareType = tareType;
	}

	@XmlElement(name = "Lwh_Uom")
	public String getLwhUOM() {
		return lwhUOM;
	}

	public void setLwhUOM(String lwhUOM) {
		this.lwhUOM = lwhUOM;
	}

	@XmlElement(name = "Length")
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	@XmlElement(name = "Width")
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@XmlElement(name = "Height")
	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@XmlElement(name = "Liquid_Volume_Uom")
	public String getLiquidVolumeUOM() {
		return liquidVolumeUOM;
	}

	public void setLiquidVolumeUOM(String liquidVolumeUOM) {
		this.liquidVolumeUOM = liquidVolumeUOM;
	}

	@XmlElement(name = "Liquid_Volume")
	public String getLiquidVolume() {
		return liquidVolume;
	}

	public void setLiquidVolume(String liquidVolume) {
		this.liquidVolume = liquidVolume;
	}

	@XmlElement(name = "Statistics_Cube")
	public String getStatisticsCube() {
		return statisticsCube;
	}

	public void setStatisticsCube(String statisticsCube) {
		this.statisticsCube = statisticsCube;
	}

	@XmlElement(name = "Weight_Uom")
	public String getWeightUOM() {
		return weightUOM;
	}

	public void setWeightUOM(String weightUOM) {
		this.weightUOM = weightUOM;
	}

	@XmlElement(name = "Weight")
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	@XmlElement(name = "Net_Weight")
	public String getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}

	@XmlElement(name = "Presentation_Method")
	public String getPresentationMethod() {
		return presentationMethod;
	}

	public void setPresentationMethod(String presentationMethod) {
		this.presentationMethod = presentationMethod;
	}
	
	

}
