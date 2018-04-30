package com.belk.car.app.webapp.forms;

import java.util.ArrayList;
import java.util.List;

import com.belk.car.app.dto.ProductTypeDTO;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;

public class ProductTypeGroupForm {

	private String id;
	private ProductGroup productGroup;
	private String name;
	private String description;
	private String statusCd;
	private String action;
	//Associated Product Types for this group
	private List<ProductType> productTypes = new ArrayList<ProductType>(0);
	//Non Associated product types for this group
	private List<ProductTypeDTO> productTypesList = new ArrayList<ProductTypeDTO>(0);
	//Gets the new Product types that are selected in the association form.
	private String[] newProductTypeArr; 
	private List<ProductGroup> productGroupList = new ArrayList<ProductGroup>(0);
	private ProductType chProductType;
	private String productTypeID;
	private String newProductGroupID;

	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String toString() {
		StringBuffer sbObjectValue = new StringBuffer();
		sbObjectValue.append("\nid --" + id);
		sbObjectValue.append("\nname --" + name);
		sbObjectValue.append("\ndescription --" + description);
		sbObjectValue.append("\nstatusCd --" + statusCd);
		sbObjectValue.append("\naction --" + action);
		return sbObjectValue.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ProductType> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<ProductType> productTypes) {
		this.productTypes = productTypes;
	}

	public List<ProductTypeDTO> getProductTypesList() {
		return productTypesList;
	}

	public void setProductTypesList(List<ProductTypeDTO> productTypesList) {
		this.productTypesList = productTypesList;
	}

	public void setNewProductTypeArr(String[] newProductTypeArr) {
		this.newProductTypeArr = newProductTypeArr;
	}

	public String[] getNewProductTypeArr() {
		return newProductTypeArr;
	}

	public List<ProductGroup> getProductGroupList() {
		return productGroupList;
	}

	public void setProductGroupList(List<ProductGroup> productGroupList) {
		this.productGroupList = productGroupList;
	}

	public ProductType getChProductType() {
		return chProductType;
	}

	public void setChProductType(ProductType chProductType) {
		this.chProductType = chProductType;
	}

	/**
	 * @return the productTypeID
	 */
	public String getProductTypeID() {
		return productTypeID;
	}

	/**
	 * @param productTypeID the productTypeID to set
	 */
	public void setProductTypeID(String productTypeID) {
		this.productTypeID = productTypeID;
	}

	/**
	 * @return the newProductGroupID
	 */
	public String getNewProductGroupID() {
		return newProductGroupID;
	}

	/**
	 * @param newProductGroupID the newProductGroupID to set
	 */
	public void setNewProductGroupID(String newProductGroupID) {
		this.newProductGroupID = newProductGroupID;
	}

}
