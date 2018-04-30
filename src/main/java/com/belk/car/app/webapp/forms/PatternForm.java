package com.belk.car.app.webapp.forms;

import com.belk.car.app.model.VendorStyle;

public class PatternForm extends VendorStyle  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6099774321313615467L;

	private Long patternId;
	private Short classNumber;
	private String vendorStyleTypeCode;
	private String productTypeId;

	public Short getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(Short classNumber) {
		this.classNumber = classNumber;
	}

	public String getVendorStyleTypeCode() {
		return vendorStyleTypeCode;
	}

	public void setVendorStyleTypeCode(String vendorStyleTypeCode) {
		this.vendorStyleTypeCode = vendorStyleTypeCode;
	}

	public String getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}

	public Long getPatternId() {
		return patternId;
	}

	public void setPatternId(Long patternId) {
		this.patternId = patternId;
	}

}
