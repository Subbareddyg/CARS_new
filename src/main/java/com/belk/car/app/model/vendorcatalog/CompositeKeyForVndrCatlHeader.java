/**
 * This is the composite key class for table name VEDNOR_CATALOG_HEADER
 * 
 * @version 1.0 14 Jan 2010
 * @author afusy07	 : Priyanka Gadia
 */
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class CompositeKeyForVndrCatlHeader implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4906238715318683240L;
	private Long vendorCatalogId;
	public CompositeKeyForVndrCatlHeader() {
		super();
		// TODO Auto-generated constructor stub
	}
	private Long vendorCatalogHdrFldNum;
	
	@Column(name = "VENDOR_CATALOG_ID", nullable = false, precision = 12, scale = 0)
	public Long getVendorCatalogId() {
		return vendorCatalogId;
	}
	public void setVendorCatalogId(Long vendorCatalogId) {
		this.vendorCatalogId = vendorCatalogId;
	}
	@Column(name = "VNDR_CATL_HDR_FLD_NUM", nullable = false, precision = 12, scale = 0)
	public Long getVendorCatalogHdrFldNum() {
		return vendorCatalogHdrFldNum;
	}
	public void setVendorCatalogHdrFldNum(Long vendorCatalogHdrFldNum) {
		this.vendorCatalogHdrFldNum = vendorCatalogHdrFldNum;
	}
	
	public CompositeKeyForVndrCatlHeader(Long vendorCatalogId,
			Long vendorCatalogHdrFldNum) {
		super();
		this.vendorCatalogId = vendorCatalogId;
		this.vendorCatalogHdrFldNum = vendorCatalogHdrFldNum;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((vendorCatalogHdrFldNum == null) ? 0
						: vendorCatalogHdrFldNum.hashCode());
		result = prime * result
				+ ((vendorCatalogId == null) ? 0 : vendorCatalogId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		CompositeKeyForVndrCatlHeader other = (CompositeKeyForVndrCatlHeader) obj;
		if (vendorCatalogHdrFldNum == null) {
			if (other.vendorCatalogHdrFldNum != null){
				return false;
			}
		}
		else if (!vendorCatalogHdrFldNum.equals(other.vendorCatalogHdrFldNum)){
			return false;
		}
		if (vendorCatalogId == null) {
			if (other.vendorCatalogId != null){
				return false;
			}
		}
		else if (!vendorCatalogId.equals(other.vendorCatalogId)){
			return false;
		}
		return true;
	}

	
}
