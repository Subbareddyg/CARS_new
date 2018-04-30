package com.belk.car.app.webapp.forms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;

public class ProductTypeForm {

	private ProductType productType;
	private String name;
	private String description;
	//private String classId;
	private String statusCd;
	private String action;
	/**
	 * Variable define productGroupID 
	 */
	private String productGroupID;
	/**
	 * Variable ProductGroups List.
	 */
	private List<ProductGroup> productGroups = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(ProductGroup.class)); 
	private Set<Classification> classifications = new HashSet<Classification>(0);
	private List<Classification> classificationList = new ArrayList<Classification>(0);
	
	
	//Default Constructor
	public ProductTypeForm() { }
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the productType
	 */
	public ProductType getProductType() {
		return productType;
	}


	/**
	 * @param productType the productType to set
	 */
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}


	/**
	 * @return the statusCd
	 */
	public String getStatusCd() {
		return statusCd;
	}


	/**
	 * @param statusCd the statusCd to set
	 */
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}


	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}


	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}


	/**
	 * @return the classificationList
	 */
	public List<Classification> getClassificationList() {
		return classificationList;
	}


	/**
	 * @param classificationList the classificationList to set
	 */
	public void setClassificationList(List<Classification> classificationList) {
		this.classificationList = classificationList;
	}


	/**
	 * @return the classifications
	 */
	public Set<Classification> getClassifications() {
		return classifications;
	}


	/**
	 * @param classifications the classifications to set
	 */
	public void setClassifications(Set<Classification> classifications) {
		this.classifications = classifications;
	}


	/**
	 * @return the productGroupID
	 */
	public String getProductGroupID() {
		return productGroupID;
	}


	/**
	 * @param productGroupID the productGroupID to set
	 */
	public void setProductGroupID(String productGroupID) {
		this.productGroupID = productGroupID;
	}


	/**
	 * @return the productGroups
	 */
	public List<ProductGroup> getProductGroups() {
		return productGroups;
	}


	/**
	 * @param productGroups the productGroups to set
	 */
	public void setProductGroups(List<ProductGroup> productGroups) {
		this.productGroups = productGroups;
	}
	

}
