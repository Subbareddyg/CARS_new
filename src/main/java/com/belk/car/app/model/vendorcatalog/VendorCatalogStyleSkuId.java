
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VendorCatalogStyleSkuId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 325769072198832729L;
	private long vendorCatalogId;
	private String vendorStyleId;
	private String vendorUPC;

	public VendorCatalogStyleSkuId(long vendorCatalogId,
			String vendorStyleId, String vendorUPC) {
		super();
		this.vendorCatalogId = vendorCatalogId;
		this.vendorStyleId = vendorStyleId;
		this.vendorUPC = vendorUPC;
	}

	public VendorCatalogStyleSkuId() {
		// TODO Auto-generated constructor stub
	}

	@Column(name = "VENDOR_CATALOG_ID",  nullable = false)
	public long getVendorCatalogId() {
		return vendorCatalogId;
	}

	public void setVendorCatalogId(long vendorCatalogId) {
		this.vendorCatalogId = vendorCatalogId;
	}

	@Column(name = "VENDOR_STYLE_ID",  nullable = false )
	public String getVendorStyleId() {
		return vendorStyleId;
	}

	public void setVendorStyleId(String vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}

	@Column(name = "VENDOR_UPC",  nullable = false)
	public String getVendorUPC() {
		return vendorUPC;
	}

	public void setVendorUPC(String vendorUPC) {
		this.vendorUPC = vendorUPC;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (vendorCatalogId ^ (vendorCatalogId >>> 32));
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
		VendorCatalogStyleSkuId other = (VendorCatalogStyleSkuId) obj;
		if (vendorCatalogId != other.vendorCatalogId)
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

	
}
