package com.belk.car.app.webapp.forms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.belk.car.app.dto.MissingAttributeValue;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ValidationExpression;
import com.belk.car.app.model.Vendor;

public class AttributeForm {

	private String submit;
	private Attribute attribute;
	private String name;
	private String description;
	private String blueMartiniAttribute;
	private String displayName;
	private boolean prePopluate;
	private String attrTypeCd; 
	private String validationExpression = ValidationExpression.NONE ;
	private String htmlDisplayTypeCd;
	private String displayType;
	private String dataTypeCd;
	private String validationRule;
	private boolean prePopulate;
	private String defaultValue;
	private String action;
	private Boolean isDisplayable;
	private Boolean isSearchable;
	// Variable added for Outfit Management 
	private Boolean isOutfit;
	
	private Boolean isPYG ;
	private String attributeValues;
	private String carAttributeIds;
	private boolean isRequired;
	 
	private String delimiter = "~|";
	private Set<Department> departments = new HashSet<Department>(0);
	private Set<ProductType> products = new HashSet<ProductType>(0);
	private Set<Classification> classifications = new HashSet<Classification>(0);
	private Set<Vendor> vendors = new HashSet<Vendor>(0);
	private List<Classification> classificationList = new ArrayList<Classification>();
	private List<CarAttribute> carAttributes = new ArrayList<CarAttribute>();
	private List<String> attributeIds = new ArrayList<String>();
	
	private List<MissingAttributeValue> missingAttributeValues = new ArrayList<MissingAttributeValue>();	
	
	/**
	 * @return the submit
	 */
	public String getSubmit() {
		return submit;
	}


	/**
	 * @param submit the submit to set
	 */
	public void setSubmit(String submit) {
		this.submit = submit;
	}


	//Default Constructor
	public AttributeForm() { }
	
	
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
	 * @return the blueMartiniAttribute
	 */
	public String getBlueMartiniAttribute() {
		return blueMartiniAttribute;
	}
	/**
	 * @param blueMartiniAttribute the blueMartiniAttribute to set
	 */
	public void setBlueMartiniAttribute(String blueMartiniAttribute) {
		this.blueMartiniAttribute = blueMartiniAttribute;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the prePopluate
	 */
	public boolean isPrePopluate() {
		return prePopluate;
	}
	/**
	 * @param prePopluate the prePopluate to set
	 */
	public void setPrePopluate(boolean prePopluate) {
		this.prePopluate = prePopluate;
	}
	/**
	 * @return the attrTypeCd
	 */
	public String getAttrTypeCd() {
		return attrTypeCd;
	}
	/**
	 * @param attrTypeCd the attrTypeCd to set
	 */
	public void setAttrTypeCd(String attrTypeCd) {
		this.attrTypeCd = attrTypeCd;
	}
	/**
	 * @return the validationExpression
	 */
	public String getValidationExpression() {
		return validationExpression;
	}
	/**
	 * @param validationExpression the validationExpression to set
	 */
	public void setValidationExpression(String validationExpression) {
		this.validationExpression = validationExpression;
	}
	/**
	 * @return the htmlDisplayTypeCd
	 */
	public String getHtmlDisplayTypeCd() {
		return htmlDisplayTypeCd;
	}
	/**
	 * @param htmlDisplayTypeCd the htmlDisplayTypeCd to set
	 */
	public void setHtmlDisplayTypeCd(String htmlDisplayTypeCd) {
		this.htmlDisplayTypeCd = htmlDisplayTypeCd;
	}
	/**
	 * @return the displayType
	 */
	public String getDisplayType() {
		return displayType;
	}
	/**
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	/**
	 * @return the dataTypeCd
	 */
	public String getDataTypeCd() {
		return dataTypeCd;
	}
	/**
	 * @param dataTypeCd the dataTypeCd to set
	 */
	public void setDataTypeCd(String dataTypeCd) {
		this.dataTypeCd = dataTypeCd;
	}
	/**
	 * @return the validationRule
	 */
	public String getValidationRule() {
		return validationRule;
	}
	/**
	 * @param validationRule the validationRule to set
	 */
	public void setValidationRule(String validationRule) {
		this.validationRule = validationRule;
	}
	/**
	 * @return the prePopulate
	 */
	public boolean isPrePopulate() {
		return prePopulate;
	}
	/**
	 * @param prePopulate the prePopulate to set
	 */
	public void setPrePopulate(boolean prePopulate) {
		this.prePopulate = prePopulate;
	}
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the attributeValues
	 */
	public String getAttributeValues() {
		return attributeValues;
	}


	/**
	 * @param attributeValues the attributeValues to set
	 */
	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}

	/**
	 * @return the isDisplayable
	 */
	public Boolean getIsDisplayable() {
		return isDisplayable;
	}


	/**
	 * @param isDisplayable the isDisplayable to set
	 */
	public void setIsDisplayable(Boolean isDisplayable) {
		this.isDisplayable = isDisplayable;
	}


	/**
	 * @return the isSearchable
	 */
	public Boolean getIsSearchable() {
		return isSearchable;
	}


	/**
	 * @param isSearchable the isSearchable to set
	 */
	public void setIsSearchable(Boolean isSearchable) {
		this.isSearchable = isSearchable;
	}
	
	/* -- Start of code added for Outfit Management -- */

	/**
	 * @return the isOutfit
	 */
	public Boolean getIsOutfit() {
		return isOutfit;
	}


	/**
	 * @param isOutfit the isOutfit to set
	 */
	public void setIsOutfit(Boolean isOutfit) {
		this.isOutfit = isOutfit;
	}

	/* -- End of code added for Outfit Management -- */
	
	/**
	 * @return the departments
	 */
	public Set<Department> getDepartments() {
		return departments;
	}

	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}


	/**
	 * @return the vendors
	 */
	public Set<Vendor> getVendors() {
		return vendors;
	}


	/**
	 * @param vendors the vendors to set
	 */
	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}


	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		return attribute;
	}


	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}


	/**
	 * @return the products
	 */
	public Set<ProductType> getProducts() {
		return products;
	}


	/**
	 * @param products the products to set
	 */
	public void setProducts(Set<ProductType> products) {
		this.products = products;
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
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}


	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}


	public String getAction() {
		return action;
	}


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
	 * @return the carAttributes
	 */
	public List<CarAttribute> getCarAttributes() {
		return carAttributes;
	}


	/**
	 * @param carAttributes the carAttributes to set
	 */
	public void setCarAttributes(List<CarAttribute> carAttributes) {
		this.carAttributes = carAttributes;
	}


	/**
	 * @return the attributeIds
	 */
	public List<String> getAttributeIds() {
		return attributeIds;
	}


	/**
	 * @param attributeIds the attributeIds to set
	 */
	public void setAttributeIds(List<String> attributeIds) {
		this.attributeIds = attributeIds;
	}


	/**
	 * @return the missingAttributeValues
	 */
	public List<MissingAttributeValue> getMissingAttributeValues() {
		return missingAttributeValues;
	}


	/**
	 * @param missingAttributeValues the missingAttributeValues to set
	 */
	public void setMissingAttributeValues(List<MissingAttributeValue> missingAttributeValues) {
		this.missingAttributeValues = missingAttributeValues;
	}


	/**
	 * @return the carAttributeIds
	 */
	public String getCarAttributeIds() {
		return carAttributeIds;
	}


	/**
	 * @param carAttributeIds the carAttributeIds to set
	 */
	public void setCarAttributeIds(String carAttributeIds) {
		this.carAttributeIds = carAttributeIds;
	}

	public boolean getIsRequired() {
		return isRequired;
	}
	
	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}


	public Boolean getIsPYG() {
		return isPYG;
	}


	public void setIsPYG(Boolean isPYG) {
		this.isPYG = isPYG;
	}
}
