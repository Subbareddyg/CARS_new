package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item_Complex_Pack_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemComplexPackSpecData {
	
	private Long packIdbId;
	
	private List<ItemDifferentiatorsData> differentiators;
	
	private Boolean isSellableFlag;
	
	private Boolean isOrderableFlag;
	
	private String packType;
	
	private String orderAsType;
	
	private Boolean isStockOnHandAtPackFlag;
	
	private List<ItemPackComponentData> packComponents;

	@XmlElement(name = "IDB_Id")
	public Long getPackIdbId() {
		return packIdbId;
	}

	public void setPackIdbId(Long packIdbId) {
		this.packIdbId = packIdbId;
	}

	@XmlElement(name = "Differentiators")
	public List<ItemDifferentiatorsData> getDifferentiators() {
		return differentiators;
	}

	public void setDifferentiators(List<ItemDifferentiatorsData> differentiators) {
		this.differentiators = differentiators;
	}

	@XmlElement(name = "Sellable_Flag")
	public Boolean getIsSellableFlag() {
		return isSellableFlag;
	}

	public void setIsSellableFlag(Boolean isSellableFlag) {
		this.isSellableFlag = isSellableFlag;
	}

	@XmlElement(name = "Orderable_Flag")
	public Boolean getIsOrderableFlag() {
		return isOrderableFlag;
	}

	public void setIsOrderableFlag(Boolean isOrderableFlag) {
		this.isOrderableFlag = isOrderableFlag;
	}

	@XmlElement(name = "Pack_Type")
	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	@XmlElement(name = "Order_As_Type")
	public String getOrderAsType() {
		return orderAsType;
	}

	public void setOrderAsType(String orderAsType) {
		this.orderAsType = orderAsType;
	}

	@XmlElement(name = "Stock_On_Hand_At_Pack_Flag")
	public Boolean getIsStockOnHandAtPackFlag() {
		return isStockOnHandAtPackFlag;
	}

	public void setIsStockOnHandAtPackFlag(Boolean isStockOnHandAtPackFlag) {
		this.isStockOnHandAtPackFlag = isStockOnHandAtPackFlag;
	}

	@XmlElement(name = "Component")
	public List<ItemPackComponentData> getPackComponents() {
		return packComponents;
	}

	public void setPackComponents(List<ItemPackComponentData> packComponents) {
		this.packComponents = packComponents;
	}

}
