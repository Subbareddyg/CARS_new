/**
 * This is the composite key class for table name VNDR_CATL_STY_SKU_MAST
 * 
 * @version 1.0 14 Jan 2010
 * @author afusy07	 : Priyanka Gadia
 */
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VendorCatalogStyleSkuMasterId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4870463939706763414L;
	private String vendorStyleId;
	private String vendorUPC;
	private Long vendorId;

	@Column(name = "VENDOR_STYLE_ID", nullable = false)
	public String getVendorStyleId() {
		return vendorStyleId;
	}

	public void setVendorStyleId(String vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}

	@Column(name = "VENDOR_UPC", nullable = false, length = 50)
	public String getVendorUPC() {
		return vendorUPC;
	}

	public void setVendorUPC(String vendorUPC) {
		this.vendorUPC = vendorUPC;
	}

	@Column(name = "VENDOR_ID", nullable = false, precision = 12, scale = 0)
	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public VendorCatalogStyleSkuMasterId(String vendorStyleId, String vendorUPC,
			Long vendorId) {
		super();
		this.vendorStyleId = vendorStyleId;
		this.vendorUPC = vendorUPC;
		this.vendorId = vendorId;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
		result = prime * result + ((vendorStyleId == null) ? 0 : vendorStyleId.hashCode());
		result = prime * result + ((vendorUPC == null) ? 0 : vendorUPC.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VendorCatalogStyleSkuMasterId other = (VendorCatalogStyleSkuMasterId) obj;
		if (vendorId == null) {
			if (other.vendorId != null)
				return false;
		}
		else if (!vendorId.equals(other.vendorId))
			return false;
		if (vendorStyleId == null) {
			if (other.vendorStyleId != null)
				return false;
		}
		else if (!vendorStyleId.equals(other.vendorStyleId))
			return false;
		if (vendorUPC == null) {
			if (other.vendorUPC != null)
				return false;
		}
		else if (!vendorUPC.equals(other.vendorUPC))
			return false;
		return true;
	}

	public VendorCatalogStyleSkuMasterId() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
