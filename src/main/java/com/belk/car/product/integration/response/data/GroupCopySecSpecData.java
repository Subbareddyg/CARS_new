package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the Copy_Sec_Spec.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Copy_Sec_Spec.
 */
@XmlRootElement(name = "Copy_Sec_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupCopySecSpecData {
	
	private String productCopyText;
	
	private String copyLine1;
	
	private String copyLine2;
	
	private String copyLine3;
	
	private String copyLine4;
	
	private String copyLine5;
	
	private String copyProductName;
	
	private String copyMaterial;
	
	private String copyCare;
	
	private String copyCountryOfOrigin;
	
	private String copyExclusive;
	
	private String copyImportDomestic;
	
	private String copyCAProp65Compliant;
	
	@XmlElement(name = "Product_Copy_Text")
	public String getProductCopyText() {
		return productCopyText;
	}

	public void setProductCopyText(String productCopyText) {
		this.productCopyText = productCopyText;
	}
	
	@XmlElement(name = "Copy_Line_1")
	public String getCopyLine1() {
		return copyLine1;
	}

	public void setCopyLine1(String copyLine1) {
		this.copyLine1 = copyLine1;
	}
	
	@XmlElement(name = "Copy_Line_2")
	public String getCopyLine2() {
		return copyLine2;
	}

	public void setCopyLine2(String copyLine2) {
		this.copyLine2 = copyLine2;
	}
	
	@XmlElement(name = "Copy_Line_3")
	public String getCopyLine3() {
		return copyLine3;
	}

	public void setCopyLine3(String copyLine3) {
		this.copyLine3 = copyLine3;
	}
	
	@XmlElement(name = "Copy_Line_4")
	public String getCopyLine4() {
		return copyLine4;
	}

	public void setCopyLine4(String copyLine4) {
		this.copyLine4 = copyLine4;
	}
	
	@XmlElement(name = "Copy_Line_5")
	public String getCopyLine5() {
		return copyLine5;
	}

	public void setCopyLine5(String copyLine5) {
		this.copyLine5 = copyLine5;
	}
	
	@XmlElement(name = "Copy_Product_Name")
	public String getCopyProductName() {
		return copyProductName;
	}

	public void setCopyProductName(String copyProductName) {
		this.copyProductName = copyProductName;
	}
	
	@XmlElement(name = "Copy_Material")
	public String getCopyMaterial() {
		return copyMaterial;
	}

	public void setCopyMaterial(String copyMaterial) {
		this.copyMaterial = copyMaterial;
	}
	
	@XmlElement(name = "Copy_Care")
	public String getCopyCare() {
		return copyCare;
	}

	public void setCopyCare(String copyCare) {
		this.copyCare = copyCare;
	}
	
	@XmlElement(name = "Copy_Country_of_Origin")
	public String getCopyCountryOfOrigin() {
		return copyCountryOfOrigin;
	}

	public void setCopyCountryOfOrigin(String copyCountryOfOrigin) {
		this.copyCountryOfOrigin = copyCountryOfOrigin;
	}
	
	@XmlElement(name = "Copy_Exclusive")
	public String getCopyExclusive() {
		return copyExclusive;
	}

	public void setCopyExclusive(String copyExclusive) {
		this.copyExclusive = copyExclusive;
	}
	
	@XmlElement(name = "Copy_Import_Domestic")
	public String getCopyImportDomestic() {
		return copyImportDomestic;
	}

	public void setCopyImportDomestic(String copyImportDomestic) {
		this.copyImportDomestic = copyImportDomestic;
	}
	
	@XmlElement(name = "Copy_CAProp65_Compliant")
	public String getCopyCAProp65Compliant() {
		return copyCAProp65Compliant;
	}

	public void setCopyCAProp65Compliant(String copyCAProp65Compliant) {
		this.copyCAProp65Compliant = copyCAProp65Compliant;
	}
}
