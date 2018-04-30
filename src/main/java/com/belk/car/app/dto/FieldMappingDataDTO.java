package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class holds the value for the field data mapping.
 * 
 * @author afusxs7 : Subbu.
 */
public class FieldMappingDataDTO implements Serializable {

	private static final long serialVersionUID = 3209174145906800897L;
	private String vendorHeaderField;
	private String vendorField;
	private boolean mapped;
	private String carValue;
	private List<String> carValueList = new ArrayList<String>(0);

	public String getVendorHeaderField() {
		return vendorHeaderField;
	}

	public void setVendorHeaderField(String vendorHeaderField) {
		this.vendorHeaderField = vendorHeaderField;
	}

	public String getVendorField() {
		return vendorField;
	}

	public void setVendorField(String vendorField) {
		this.vendorField = vendorField;
	}

	public boolean isMapped() {
		return mapped;
	}

	public void setMapped(boolean mapped) {
		this.mapped = mapped;
	}

	public String getCarValue() {
		return carValue;
	}

	public void setCarValue(String carValue) {
		this.carValue = carValue;
	}

	public List<String> getCarValueList() {
		return carValueList;
	}

	public void setCarValueList(List<String> carValueList) {
		this.carValueList = carValueList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((vendorHeaderField == null) ? 0 : vendorHeaderField
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FieldMappingDataDTO))
			return false;
		FieldMappingDataDTO other = (FieldMappingDataDTO) obj;
		if (vendorHeaderField == null) {
			if (other.vendorHeaderField != null)
				return false;
		} else if (!vendorHeaderField.equals(other.vendorHeaderField))
			return false;
		return true;
	}
	
	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("vendorHeaderField", this.vendorHeaderField)
                .append("vendorField", this.vendorField)
                .append("mapped", this.mapped)
                .append("carValue", this.carValue)
                .append("carValueList", this.carValueList).toString();
    }

}
