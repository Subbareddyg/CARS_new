package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.List;


public class OutfitFormDTO implements Serializable {
	
	private static final long serialVersionUID = 509429231281639843L;
	
	private String outfitName;
	private String description;
	private boolean bmProductName=false;
	private List<ChildCarDTO> childCars;
	
	public String getOutfitName() {
		return outfitName;
	}
	public void setOutfitName(String outfitName) {
		this.outfitName = outfitName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isBmProductName() {
		return bmProductName;
	}
	public void setBmProductName(boolean bmProductName) {
		this.bmProductName = bmProductName;
	}
	public List<ChildCarDTO> getChildCars() {
		return childCars;
	}
	public void setChildCars(List<ChildCarDTO> childCars) {
		this.childCars = childCars;
	}
}
