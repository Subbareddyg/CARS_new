/**
 * This is the composite key class for table name VENDOR_CATALOG_DATA_MAPPING
 * 
 * @version 1.0 14 Jan 2010
 * @author afusy07	 : Priyanka Gadia
 */
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompositeKeyForDataMapping implements java.io.Serializable{

	public CompositeKeyForDataMapping() {
		super();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5215741235666667181L;

	private long vendorCatalogtTemplateId;
	private String vendorCatalogHeaderFieldName;
	 
	@Column(name = "VENDOR_CATALOG_TMPL_ID", nullable = false)
	public long getVendorCatalogtTemplateId() {
		return vendorCatalogtTemplateId;
	}
	public void setVendorCatalogtTemplateId(long vendorCatalogtTemplateId) {
		this.vendorCatalogtTemplateId = vendorCatalogtTemplateId;
	}
	
	@Column(name = "VNDR_CATL_HDR_FLD_NAME", nullable = false)
	public String getVendorCatalogHeaderFieldName() {
		return vendorCatalogHeaderFieldName;
	}
	
	public void setVendorCatalogHeaderFieldName(String vendorCatalogHeaderFieldName) {
		this.vendorCatalogHeaderFieldName = vendorCatalogHeaderFieldName;
	}
	
	public CompositeKeyForDataMapping(long vendorCatalogtTemplateId,
			String vendorCatalogHeaderFieldName) {
		super();
		this.vendorCatalogtTemplateId = vendorCatalogtTemplateId;
		this.vendorCatalogHeaderFieldName = vendorCatalogHeaderFieldName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((vendorCatalogHeaderFieldName == null) ? 0
						: vendorCatalogHeaderFieldName.hashCode());
		result = prime
				* result
				+ (int) (vendorCatalogtTemplateId ^ (vendorCatalogtTemplateId >>> 32));
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
		CompositeKeyForDataMapping other = (CompositeKeyForDataMapping) obj;
		if (vendorCatalogHeaderFieldName == null) {
			if (other.vendorCatalogHeaderFieldName != null){
				return false;
			}
		}
		else if (!vendorCatalogHeaderFieldName
				.equalsIgnoreCase(other.vendorCatalogHeaderFieldName)){
			return false;
		}
		if (vendorCatalogtTemplateId != other.vendorCatalogtTemplateId){
			return false;
		}
		return true;
	}
	
	
	
	

}
