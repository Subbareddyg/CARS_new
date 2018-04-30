package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VendorStylePIMAttributeId implements java.io.Serializable{

	private static final long serialVersionUID = 13487348743646L;
	long vendorStyleId;
	long pimAttrId;
	
	public VendorStylePIMAttributeId(){}
	
	public VendorStylePIMAttributeId(long vendorStyleId, long pimAttrId ){
		this.vendorStyleId = vendorStyleId;
		this.pimAttrId = pimAttrId;
	}

	@Column(name = "VENDOR_STYLE_ID", nullable = false, precision = 12, scale = 0)
	public long getVendorStyleId() {
		return vendorStyleId;
	}

	public void setVendorStyleId(long vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}

	@Column(name = "PIM_ATTR_ID", nullable = false, precision = 12, scale = 0)
	public long getPimAttrId() {
		return pimAttrId;
	}

	public void setPimAttrId(long pimAttrId) {
		this.pimAttrId = pimAttrId;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VendorStylePIMAttributeId))
			return false;
		VendorStylePIMAttributeId castOther = (VendorStylePIMAttributeId) other;

		return (this.getPimAttrId() == castOther.getPimAttrId())
				&& (this.getVendorStyleId() == castOther.getVendorStyleId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getPimAttrId();
		result = 37 * result + (int) this.getVendorStyleId();
		return result;
	}
}
